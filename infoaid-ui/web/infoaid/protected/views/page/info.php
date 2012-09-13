<div id="page-<?php echo $page->id; ?>" class="page-info">

	<header class="info">
		<?php $this->renderPartial('header', array('slug'=>$slug,'id'=>$page->id)); ?>
	</header>

	<section class="posts" ng-app="post">
		<header>
			<h1>Status Update</h1>
		</header>

		<div ng-view></div>
	</section>
</div>

<script>
	var apiUrl = "<?php echo $this->createUrl("api/page") ;?>/:slug/recent_post";
</script>

<?php Yii::app()->clientScript->registerScriptFile(Yii::app()->baseUrl .'/js/main/postService.js'); ?>

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