angular.module('commentService', ['ngResource']).
	factory('Comment', function ($resource) {
		var Comment = $resource(baseUrl + '/index.php/api/post/:postId/comment');

		return Comment;
	});

function CommentCtrl($scope, Comment) {

	var lastRowLastUpdated = function () {
		return $($scope.comments).last().get(0).lastUpdated;
	};

	if (! angular.isUndefined($scope.post.comments)) {
		$scope.comments = $scope.post.comments.slice(0, 3).reverse();
	}

	$scope.loadMore = function () {
		Comment.query({
			postId: $scope.post.id,
			until: lastRowLastUpdated(),
			limit: 10
		}, function (comments) {
			angular.forEach(comments, function (comment) {
				$scope.comments.unshift(comment);
			});
		});
	};

}
