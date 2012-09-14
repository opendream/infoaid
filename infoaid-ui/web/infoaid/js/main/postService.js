angular.module('postService', ['ngResource']).
	factory('Post', function ($resource) {
		var Post = $resource(baseUrl + '/index.php/api/page/:slug/recent_post');

		return Post;
	});