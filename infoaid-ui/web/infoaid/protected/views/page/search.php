<div id='page-search' class='page-search' ng-app="page">
	<header class="search">
		<div class="search-header"><span>Search Places</span></div>
	</header>
	<div ng-controller="searchController" class="search-input">
		<form class="form-search" name="search-form">
			<div class="input-append">
				<span><input style="width: 200px" class="span2 search-query" placeholder="2 more character..." name="word" type="text" ng-model="word" pattern=".{2,}" required/></span>
				<span><input class="btn" type="button" value="Search" ng-click="search()"></span>
			</div>
		</form>

		<div id='result-search'>
			<ul class="unstyled">
				<li ng-repeat="page in pages" ng-bind-html-unsafe="page.body"></li>
			<ul>
		</div>

		<div class="load-more">
			<button class="btn" ng-click="loadMore()">
				<i class="icon icon-plus"></i> Load more
			</button>
		</div>
	<div>
</div>
<script>

	angular.module('pageService', ['ngResource']).
		factory('Page', function ($resource) {
			var Page = $resource('<?php echo $this->createUrl("api/pageSearch"); ?>');

			return Page;
		});

	angular.module('page', ['pageService']);

	function searchController($scope, Page) {
		var urlSearch = "<?php echo $this->createUrl('api/pageSearch'); ?>";
		$scope.word = '';
		$scope.pages = [];
		$scope.search = function() {
			if($scope.word.length >= 2) {
				$scope.pages = Page.query({
					word: $scope.word,
					offset: 0
				});
			}
		}

		$scope.loadMore = function() {
			Page.query({
				word: $scope.word,
				offset: 10
			}, function (pages) {
				angular.forEach(pages, function (page) {
					$scope.pages.push(page);
				});
			});
			console.log($scope.pages)
		};
	}

	
</script>