angular.module('commentService', ['ngResource']).
	factory('Comment', function ($resource) {
		var Comment = $resource(baseUrl + '/index.php/api/post/:postId/comments');

		return Comment;
	});

function CommentCtrl($scope, Comment) {

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

}
