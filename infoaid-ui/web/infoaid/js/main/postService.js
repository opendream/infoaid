angular.module('postService', ['ngResource']).
	factory('Post', function ($resource) {
		var Post = $resource(baseUrl + '/api/page/:slug/recent_post');

		return Post;
	});

angular
	.module('post', ['postService', 'commentService', 'time', 'headerService'])
	.directive('post', function ($compile) {
		return {
			restrict: 'A',
			compile: function (element, attrs) {
				return {
					pre: function ($scope, $element, $attrs) {
						var template = Templates.post[$scope.post.class]
										|| Templates.post.MessagePost;

						$element.html(template);
						$compile($element.contents())($scope);
					}
				}
			}
		};
	});