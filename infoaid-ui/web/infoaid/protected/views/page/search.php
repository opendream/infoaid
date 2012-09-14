<div id='page-search' class='page-search' ng-app="page">
	<header class="search">
		<div class="search-header"><span>Search Places</span></div>
	</header>
	<div ng-controller="searchController" class="search-input control-group" id="search-body">
		<span><label class="text-error">{{validate}}</label></span>
		<form class="form-search" name="search-form" ng-submit="search()">
			<div class="input-append">
				<span><input id="word" style="width: 200px" class="span2 search-query" ng-change="change()" placeholder="2 more character..." name="word" type="text" ng-model="word"/></span>
				<span><input class="btn" type="submit" value="Search"></span>
			</div>
		</form>
		<div id='result-search'>
			<ul class="unstyled">
				<li ng-repeat="page in pages" ng-bind-html-unsafe="page.body"></li>
			<ul>
		</div>
		<div id='result-search-error'></div>
		<div id="loading" class="ajax-loading"></div>
		<div class="load-more" id="load-more" ng-show="pages">
			<button class="btn" ng-click="loadMore()">
				<i class="icon icon-plus"></i> Load more
			</button>
		</div>
	</div>
</div>
<script>
	$('#load-more').hide();
	angular.module('pageService', ['ngResource']).
		factory('Page', function ($resource) {
			var Page = $resource('<?php echo $this->createUrl("api/pageSearch"); ?>');

			return Page;
		});

	angular.module('page', ['pageService']);

	function searchController($scope, Page) {
		var opts = {
		  lines: 13, // The number of lines to draw
		  length: 7, // The length of each line
		  width: 4, // The line thickness
		  radius: 10, // The radius of the inner circle
		  corners: 1, // Corner roundness (0..1)
		  rotate: 0, // The rotation offset
		  color: '#000', // #rgb or #rrggbb
		  speed: 1, // Rounds per second
		  trail: 60, // Afterglow percentage
		  shadow: false, // Whether to render a shadow
		  hwaccel: false, // Whether to use hardware acceleration
		  className: 'spinner', // The CSS class to assign to the spinner
		  zIndex: 2e9, // The z-index (defaults to 2000000000)
		  top: 'auto', // Top position relative to parent in px
		  left: 'auto' // Left position relative to parent in px
		};
		$scope.word = '';
		$scope.pages = [];
		$scope.search = function() {
			if($scope.word != '' && $scope.word != null && $scope.word.length >= 2) {
				$('#load-more').hide();
				var target = document.getElementById('loading');
				var spinner = new Spinner(opts).spin(target);
				var pages = Page.query({
					word: $scope.word,
					offset: 0
				}, function(resp) {
					if(resp.length == 0) {
						$('#load-more').hide();
						$('#result-search-error').addClass("text-error");
						$('#result-search-error').html('Not found this place');
					} else {
						$('#result-search-error').html('');
						$('#load-more').show();
					}
					spinner.stop()
				});
				$scope.pages = pages;
			} else {
				$('#search-body').addClass("error");
				$('#word').tooltip({'title':'Please type 2 more character'}).tooltip('show');
			}
		}

		$scope.change = function() {
			if($scope.word != '' && $scope.word != null) {
				if($scope.word.length < 2) {
					$('#search-body').addClass("error");
					$('#word').tooltip({'title':'Please type 2 more character'}).tooltip('show');
				} else {
					$('#search-body').removeClass("error");
					$('#word').tooltip({'title':'Please type 2 more character'}).tooltip('hide');
				}
			}
		}

		$scope.loadMore = function() {
			var target = document.getElementById('loading');
			var spinner = new Spinner(opts).spin(target);
			Page.query({
				word: $scope.word,
				offset: $scope.pages.length
			}, function (pages) {
				angular.forEach(pages, function (page) {
					$scope.pages.push(page);
				});
				spinner.stop();
			});
		};
	}
</script>