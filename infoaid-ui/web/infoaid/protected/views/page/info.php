<div id="page-<?php echo $page->id; ?>" class="page-info">

	<header class="info">
		<h1><?php include '_header.php'; ?></h1>
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
	var slug = "<?php echo $slug; ?>";

	var lastRowDateCreated = function () {
		return $($scope.posts).last().get(0).dateCreated;
	};

	$scope.posts = Post.query({slug: slug});

	$scope.loadMore = function () {
		Post.query({
			slug: slug,
			until: lastRowDateCreated(),
			limit: 10
		}, function (posts) {
			angular.forEach(posts, function (post) {
				$scope.posts.push(post);
			});
		});
	};
}
</script>