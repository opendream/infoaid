angular.module('postService', ['ngResource']).
	factory('Post', function ($resource) {
		var Post = $resource(apiUrl);

		return Post;
	});