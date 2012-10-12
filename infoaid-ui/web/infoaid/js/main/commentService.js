angular.module('commentService', ['ngResource']).
	factory('Comment', function ($resource) {
		var Comment = $resource(baseUrl + '/api/post/:postId/comments');
		return Comment;
	}).
	factory('PostComment', function ($resource) {
		var PostComment = $resource(baseUrl + '/api/comment');
		return PostComment;
	}).
	factory('DeleteComment', function ($resource) {
		var DeleteComment = $resource(baseUrl + '/api/deleteComment/:commentId');
		return DeleteComment;
	}).
	factory('PostMessage', function ($resource) {
		var PostMessage = $resource(baseUrl + '/api/postMessage', {}, {
			'get' : { method: 'post' }
		});
		return PostMessage;
	}).
	factory('DeletePost', function ($resource) {
		var DeletePost = $resource(baseUrl + '/api/deletePost/:postId');
		return DeletePost;
	}).
	factory('Items', function ($resource) {
		var Items = $resource(baseUrl + '/api/items');
		return Items;
	}).
	factory('CreateItem', function ($resource) {
		var CreateItem = $resource(baseUrl + '/api/createItem');
		return CreateItem;
	}).
	factory('PostRequest', function ($resource) {
		var PostRequest = $resource(baseUrl + '/api/postNeed/');
		return PostRequest;
	}).
	factory('FindPost', function () {
		var FindPost = function(scope, newpost) {
			var result = {status:false};
			
			for(var i=0; i<scope.posts.length; i++){
				if(scope.posts[i].id == newpost.id ) {
					result.status =  true;
					result.index = i;
					return result;
				}
			}			
			return result;
		};
		return FindPost;
	}).
	factory('RearrangePost', function (FindPost) {
		var RearrangePost = function(scope, newPosts) {
			angular.forEach(newPosts, function(post) {
				var result = FindPost(scope, post);
				var postCount = $(scope.posts).length;

				if (postCount === 0) {
					scope.posts.push(post);
				}
				else {
					if(!result.status) {
						if($(scope.posts).first().get(0).lastActived < post.lastActived) {
							scope.posts.unshift(post);
						}
						if($(scope.posts).last().get(0).lastActived > post.lastActived ) {
							scope.posts.push(post);
						}

					} else {
						scope.posts.splice(result.index, 1, post);
					}		
				}						
			});								
		};		
		return RearrangePost;
	}).
	factory('RefreshPost', function (Post, FindPost, RearrangePost, PostsBroadcast) {
		var RefreshPost = function(scope) {
			Post(scope.target).query({slug:scope.slug}, function (newposts) {
				RearrangePost(scope, newposts);
				PostsBroadcast.prepForBroadcast('recent_post', newposts);	
			});
		};

		return RefreshPost;
	}).
	factory('PostResource', function ($resource) {
		var PostResource = $resource(baseUrl + '/api/postResource/');
		return PostResource;
	});

function PostBodyCtrl($scope, DeletePost, RefreshPost, SharedService) {	

	$scope.deletePost = function(post) {
		if(post.id) {
			var options = { postId: post.id };
			DeletePost.get(options, function (ret) {	
				if(ret.status==1) {
					RefreshPost($scope);
					for (var i = 0, ii = $scope.posts.length; i < ii; i++) {
						if($scope.posts[i].id == ret.id) {
							$scope.posts.splice(i, 1);
							return;
						}
					}
				}
			});
		}
	}
}

function CommentCtrl($scope, Comment, DeletePost, PostComment, DeleteComment, Post, RefreshPost, SharedService) {
	var lastRowLastUpdated = function () {
		var comments = $($scope.comments);
		if (comments.length === 0) {
			return false;
		}
		else {
			return comments.last().get(0).lastUpdated;
		}
	};

	if (! angular.isUndefined($scope.post.comments)) {
		$scope.comments = $scope.post.comments.slice(0, 3).reverse();
	}

	$scope.$on('isJoinedBroadcast', function() {
        $scope.isjoined = SharedService.isJoined;
    });

	$scope.loadMore = function(id) {
		var options = {
				postId: $scope.post.id,
				limit: 10				
			},
			lastUpdated = lastRowLastUpdated()
		;

		if (lastUpdated) {
			options.until = lastUpdated;
		}
		
		$scope.comments = $scope.comments || [];

		Comment.query(options, function (comments) {
			angular.forEach(comments, function (newcomment) {
				if(!findComment(newcomment)) {
					$scope.comments.unshift(newcomment);
				}
								
			});
			if(id && ($scope.post.conversation == $scope.comments.length)) {
				var element = angular.element(id);
				element.css('visibility', 'hidden'); //visible
				//element.css('display', 'none');//none inherit block
			}
		});
	}

	$scope.submitComment = function() {
		if (this.comment && this.post.id) {	
			var options = {
				postId: this.post.id, 
				message: this.comment
			};
			
			PostComment.get(options, function (ret) {
				RefreshPost($scope);
				$scope.comment = '';
				var comment = {'id':ret.commentId, 'picSmall':ret.picSmall, 'createdBy':ret.user, 
								'message':ret.comment, 'lastUpdated':ret.lastUpdated};
				$scope.comments.push(comment);	
				$scope.comments.splice(0, 1);				
			});							
		}
	}	

	$scope.deleteComment = function(comment) {
		if(comment.id) {
			var options = { commentId: comment.id };
			DeleteComment.get(options, function (ret) {	
				RefreshPost($scope);
				for (var i = 0, ii = $scope.comments.length; i < ii; i++) {
					if($scope.comments[i].id == ret.id) {
						$scope.comments.splice(i, 1);
						return;
					}
				}
			});

		}		
	}

	function findComment(newcomment) {
		var isfound = false;
		angular.forEach($scope.comments, function (comment) {
			if(comment.id == newcomment.id ) {
				isfound =  true;
			}
		});
		return isfound;
	}
}

function PostMessageCtrl($scope, PostMessage, PostRequest, Post, Items, RefreshPost, PostResource, SharedService) {
	$scope.items = [];
	if($scope.items.length===0) {
		Items.query(function(ret) {
			angular.forEach(ret, function(i) {
				$scope.items.push(i);
			});
		});
	}

	$scope.needUnits = InfoAid.settings.icon;
	$scope.resourceUnits = InfoAid.settings.icon;

	$scope.$on('isJoinedBroadcast', function() {
        $scope.isjoined = SharedService.isJoined;
    });

    $scope.selectNeed = function (event, id) {
    	var target = $(event.currentTarget);
    	$scope.request = $('input[type="radio"]', target).val();
    	$scope.requestItemName = target.attr('data-item-name');
    	$scope.requestItemClass = target.attr('data-item-class');

    	var config = InfoAid.settings.icon[$scope.requestItemClass];
    	$scope.needUnits = config.unit.variants;
    	$scope.needUnitConfig = config.unit;
    	console.log($scope.needUnits);

    	$('button.close', id).click();
    };

    $scope.selectResource = function (event, id) {
    	var target = $(event.currentTarget);
    	$scope.resource = $('input[type="radio"]', target).val();
    	$scope.resourceItemName = target.attr('data-item-name');
    	$scope.resourceItemClass = target.attr('data-item-class');

    	$('button.close', id).click();
    };

	$scope.postMessage = function(event) {
		var button = $('.post-button button');

		if ($scope.message) {
			button.button('loading');

			var options = {
				slug: $scope.slug,
				message: $scope.message,
				picOriginal: $('#picOriginal').val(),
				picLarge: $('#picLarge').val(),
				picSmall: $('#picSmall').val(),
				csrf_token: $('.message-post-form input[name="csrf_token"]').val()
			};
			
			PostMessage.get(options, function (ret) {
				RefreshPost($scope);
				$scope.message = '';
				$('#previewImg').html('');
				$('#picSmall').val('');
				$('#picLarge').val('');
		        $('#picOriginal').val('');

		        button.button('reset');
			});							
		}
	}

	$scope.postRequest = function() {
		var options = {
			slug: $scope.slug,
			itemId: $scope.request, 
			quantity: $scope.qty * $scope.needUnit,
		};
		PostRequest.get(options, function (ret) {	
			RefreshPost($scope);

			// Get element and clear value.
			var el = $('select[ng-model="request"]');
			el.select2('val', '');
			$scope.request = '';
			$scope.qty = '';	
			$scope.needUnit = '';
		});
	}

	$scope.postResource = function() {
		var options = {
				slug: $scope.slug,
				itemId: $scope.resource,
				quantity: $scope.resourceQty
			};
		PostResource.get(options, function (ret) {
			RefreshPost($scope);

			// Get element and clear value.
			var el = $('select[ng-model="resource"]');
			el.select2('val', '');
			$scope.resource = '';
			$scope.resourceQty = '';
		});
	}
}

function ModalCtrl($scope, CreateItem) {
   $scope.setModel = function(data) {
   		var options = {
			newItem: data
		};
   		var element = angular.element('#myModal');
   		CreateItem.get(options, function (ret) {
   			$scope.newItem = '';							
		});
        element.modal('hide');        
   }

   $scope.removeNewItem = function() {
   		$scope.newItem = '';
        element.modal('hide');
   }   
}