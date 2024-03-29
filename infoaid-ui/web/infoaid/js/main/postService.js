angular.module('postService', ['ngResource']).
	factory('Post', function ($resource) {
		var Post = function(method) {
			return $resource(baseUrl + '/api/page/:slug/' + method);
		}
		return Post;
	});

angular
	.module('post', ['postService', 'commentService', 'time', 'headerService', 'itemSidebar', 'delete'])
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
	})
	.directive('colorbox', function () {
		return {
			restrict: 'C',
			link: function ($scope, $element, $attrs) {
				$element.colorbox({
					scalePhotos: true,
					maxWidth: '90%',
					maxHeight: '90%'
				});
			}
		};
	});