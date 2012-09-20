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
		var PostMessage = $resource(baseUrl + '/api/postMessage');
		return PostMessage;
	}).
	factory('DeletePost', function ($resource) {
		var DeletePost = $resource(baseUrl + '/api/deletePost/:postId');
		return DeletePost;
	});

function CommentCtrl($scope, Comment, PostComment, DeleteComment, Post) {

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

	$scope.loadMore = function() {
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
			angular.forEach(comments, function (comment) {
				//$scope.comments.unshift(comment);
				$scope.comments.push(comment);
			});
		});
	}
	
	$scope.submitComment = function() {
		if (this.comment && this.memberId && this.post.id) {	
			var options = {
				userId: this.memberId, 
				postId: this.post.id, 
				message: this.comment
			};
			
			PostComment.get(options, function (ret) {	
				refresh();	
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
			var options = { commentId: comment.id, userId: this.memberId };
			DeleteComment.get(options, function (ret) {	
				refresh();			
				for (var i = 0, ii = $scope.comments.length; i < ii; i++) {
					if($scope.comments[i].id == ret.id) {
						$scope.comments.splice(i, 1);
						return;
					}
				}
			});

		}		
	}

	

	function refresh() {
		Post.query({slug:$scope.slug}, function (posts) {					 
			var i = 0;
			angular.forEach(posts, function(post) {
				if($scope.posts.length > i) {
					$scope.posts.splice(i, 1, post);
				} else {
					$scope.posts.push(post);
				}
				i++;
			});					
		});
	}
}


function PostMessageCtrl($scope, PostMessage, Post, DeletePost) {
	$scope.postMessage = function() {
		if ($scope.message && $scope.memberId) {	
			var options = {
				slug: $scope.slug,
				userId: $scope.memberId, 
				message: $scope.message
			};
			
			PostMessage.get(options, function (ret) {	
				console.log(ret);
				refresh();	
				$scope.message = '';				
			});							
		}
	}

	$scope.deletePost = function(post) {
		console.log(post.id, this.memberId);
		if(post.id) {
			var options = { postId: post.id, userId: this.memberId };
			DeletePost.get(options, function (ret) {	
				refresh();					
			});
		}
	}

	function refresh() { // duplicated function need to fix!
		Post.query({slug:$scope.slug}, function (posts) {					 
			var i = 0;
			angular.forEach(posts, function(post) {
				if($scope.posts.length > i) {
					$scope.posts.splice(i, 1, post);
				} else {
					$scope.posts.push(post);
				}
				i++;
			});					
		});
	}

}