<div id="page-<?php echo $page->id; ?>" class="page-info">

		<header class="info">
			<div>
				<?php $this->renderPartial('sidebar'); ?>
			</div>
			<div>
				<?php $this->renderPartial('header', array('slug'=>$slug,'id'=>$page->id)); ?>
			</div>
		</header>
		
		<div class="row" ng-app="post">				
			<div ng-init="slug='<?php echo $slug ?>'"></div>
			<div class="span7">
				<section class="posts" >
					<header>
						<h1>Status Update</h1>
					</header>

					<div ng-view class="span5"></div>
				</section>
			</div>	

			<div class="span2">
				<div id="members">
				    <h2><?php echo CHtml::link('Members', array('page/members','slug'=>$slug)); ?></h2>

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