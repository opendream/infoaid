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
			<ul class="unstyled" ng-controller="HeaderCtrl">
				<div>
					<li ng-repeat="page in pages" >

						<!-- page header -->
						<div id="page-header-{{page.id}}" class='page-header span10' ng-controller="HeaderCtrl">
						    <div id="page-header-left-{{page.id}}"  >
						        <div class="page-picture" ng-switch on="page.picSmall">
						        	<a href="<?php echo Yii::app()->baseUrl; ?>/page/{{page.slug}}">
						        		<img ng-switch-when="null" src="<?php echo Yii::app()->baseUrl; ?>/media/pages/page_default_small.jpg" class="img-polaroid"></img>
						        		<img ng-switch-default src="<?php echo Yii::app()->baseUrl; ?>/{{page.picSmall}}" class="img-polaroid"></img>
						        	</a>            
						        </div>
						    </div>
						    <div id="page-header-right-{{page.id}}" class="page-header-right">
						        <div>
						            <span class='page-name'>
						            	<a href="<?php echo Yii::app()->baseUrl; ?>/page/{{page.slug}}">{{page.name}}</a>                
						            </span>                
						                <span class="edit" ng-show="page.isOwner">
						                	--
						                	<a href="<?php echo Yii::app()->baseUrl; ?>/page/{{page.slug}}/edit">Edit</a>                    
						                </span>
						        </div>
						        <div class='page-household-population'>
						            <span>{{page.household}} House            
						            </span>
						            <span>{{page.population}} Man
						            </span>
						        </div>
						        <div class='page-lat-lng'>
						            <span><b>Latitude :</b> {{page.lat}}             
						            </span>
						            <span><b>Longitude :</b> {{page.lng}}
						            </span>
						        </div>
						        <div>
						            <span class='page-needs'>
						                <b>Need : </b><span ng-repeat="need in page.needs">{{need.item}} {{need.quantity}} </span>
						            </span>
						        </div>
						    </div>						    
						    <div id="page-header-join-leave-{{page.id}}" class="page-header-join-leave">
						        <button class='btn' ng-click="handleClick()" ng-show="<?php echo Yii::app()->user->getId();?>">{{page.isJoined | JoinLabel}}</button>        
						    </div>
						    <div id="join-leave-page-loading-{{page.id}}"></div>    
						</div>
						<!-- end of page header -->
					</li>
				</div>
			<ul>
		</div>		
		<div id='result-search-error'></div>
		
		<div class="load-more" id="load-more" ng-show="pages.length">
			<button id="load-more-button" class="btn" ng-click="loadMore()">
				<i class="icon icon-plus"></i> Load more
			</button>
		</div>
		<div id="loading" class="ajax-loading"></div>
	</div>
</div>
<script>
	$('#load-more').hide();
	angular.module('pageSearchService', ['ngResource']).
		factory('Page', function ($resource) {
			var Page = $resource('<?php echo $this->createUrl("api/pageSearch"); ?>');

			return Page;
		});
	
</script>