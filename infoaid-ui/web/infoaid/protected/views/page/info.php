<div id="page-<?php echo $page->id; ?>" class="page-info">	
	<?php Yii::app()->clientScript
       ->registerScriptFile(Yii::app()->baseUrl .'/js/main/postService.js')
       ->registerScriptFile(Yii::app()->baseUrl .'/js/main/commentService.js')
       ->registerScriptFile(Yii::app()->baseUrl .'/js/controllers.js')       
	; ?>

		<header class="info">
			<?php $this->renderPartial('header', array('slug'=>$slug,'id'=>$page->id)); ?>
		</header>

		<div class="row" ng-app="post">				
			<div ng-init="slug='<?php echo $slug ?>'"></div>
			<div class="span9">
				<section class="posts" >
					<header>
						<h1>Status Update</h1>
					</header>

					<div ng-view></div>
				</section>
			</div>	

			<div class="span3">				
				<div id="members">				    
				    <h2>Members</h2>
				               
				    <div ng-controller="MemberCtrl">               
				        <ul class="unstyled">
				            <li ng-repeat="member in members">
				                <span>{{member.username}}</span>
				            </li>
				        </ul>
				    </div>				    
				</div>				
			</div>
		</div>	
</div>

<script>
angular.module('post', ['postService', 'commentService', 'time'], function ($routeProvider) {
	$routeProvider.
		when('/', {
			controller: ListCtrl,
			templateUrl: '<?php echo $this->createUrl(); ?>/static/view/post-list'
		}).
		otherwise({redirectTo: '/'});
});
</script>