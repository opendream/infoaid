<div id="page-<?php echo $page->id; ?>" class="page-info" ng-app="post">

		<header class="info">
			<div>
				<?php $this->renderPartial('sidebar'); ?>
			</div>
			<div>
				<?php $this->renderPartial('header', array('slug'=>$slug,'id'=>$page->id)); ?>
			</div>
		</header>
		
		<div class="row">				
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
				    <h2>Members</h2>

				    <div ng-controller="MemberCtrl">               
				        <ul class="unstyled">
				            <li ng-repeat="member in members">
				                <span>{{member.username}}</span>
				            </li>
				        </ul>
				    </div>
				    <div><span class='show-more-members'><?php echo CHtml::link('Show more ..', array('page/members','slug'=>$slug)); ?></span></div>
				</div>				
			</div>
		</div>	
</div>