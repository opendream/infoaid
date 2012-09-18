angular.module('postService', ['ngResource']).
	factory('Post', function ($resource) {
		var Post = $resource(baseUrl + '/api/page/:slug/recent_post');

		return Post;
	});

angular.module('post', ['postService', 'commentService', 'time'], function ($routeProvider) {
	$routeProvider.
		when('/', {
			controller: ListCtrl,
			templateUrl: baseUrl + '/page/static/view/post-list'
		}).
		otherwise({redirectTo: '/'});
});