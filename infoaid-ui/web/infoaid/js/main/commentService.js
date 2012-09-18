angular.module('commentService', ['ngResource']).
	factory('Comment', function ($resource) {
		var Comment = $resource(baseUrl + '/api/post/:postId/comments');

		return Comment;
	}).
	factory('PostComment', function ($resource) {
		var PostComment = $resource(baseUrl + '/api/comment');
		return PostComment;
	});

function CommentCtrl($scope, Comment, PostComment) {

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

	$scope.loadMore = function () {
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
				$scope.comments.unshift(comment);
			});
		});
	};
	
	$scope.submitComment = function() {
		if (this.comment && this.memberId && this.post.id) {	
			var options = {
				userId: this.memberId, 
				postId: this.post.id, 
				message: this.comment
			};
			
			var result = PostComment.save(options, function (ret) {	
				//console.log(ret.status);
				$scope.comment = '';		
				
			});					
		}
	};
	

}
