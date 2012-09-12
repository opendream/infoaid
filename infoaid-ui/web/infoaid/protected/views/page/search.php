<div id='page-search' class='page-search' ng-app="">
	<header class="search">
		<div><span><h1>Search Places</h1></span></div>
	</header>
	<div ng-controller="searchController">
		<form name="search-form">
			<span><input name="word" type="text" ng-model="word" pattern=".{2,}" required/></span>
			<span><input type="submit" ng-click="search()"></span>
		</form>
	</div>
	<div id='result-search'>
	</div>
</div>
<script>
	function searchController($scope) {
		var urlSearch = "<?php echo $this->createUrl('api/pageSearch'); ?>";
		$scope.word = '';
		$scope.search = function() {
			if($scope.word.length >= 2) {
				$.get(urlSearch, {word: $scope.word}, function (resp) {
					$('#result-search').html(resp)
		    	});
			}
		}
	}
</script>