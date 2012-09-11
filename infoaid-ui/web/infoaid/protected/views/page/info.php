<div id="page-<?php echo $page->id; ?>" class="page-info">

	<header class="info">
		<h1><?php echo $page->name; ?></h1>
	</header>

	<section class="posts" ng-app="post">
		<header>
			<h1>Status Update</h1>
		</header>

		<div ng-view></div>
	</section>
</div>

<script id="postService.js">
angular.module('postService', ['ngResource']).
	factory('Post', function ($resource) {
		var Post = $resource('<?php echo $this->createUrl("api/page"); ?>/:slug/recent_post');

		return Post;
	});
</script>

<script id="post.js">
angular.module('post', ['postService'], function ($routeProvider, $locationProvider) {

	$routeProvider.
		when('/', {
			controller: ListCtrl,
			templateUrl: '<?php echo $this->createUrl(); ?>/static/view/post-list'
		});
});

function ListCtrl($scope, Post) {
	$scope.posts = Post.query({slug: "<?php echo $slug; ?>"});
}
</script>