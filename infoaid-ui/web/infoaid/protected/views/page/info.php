<div id="page-<?php echo $page->id; ?>" class="page-info" ng-app="post">

		<header class="info">
			<div>
				<?php $this->renderPartial('sidebar', array(
							'userId' => user()->getId()						
						)); ?>
			</div>
			<div>
				<?php $this->renderPartial('header', array('slug'=>$slug,'id'=>$page->id)); ?>
			</div>
		</header>
		
		<div class="row">				
			<div ng-init="slug='<?php echo $slug ?>'; target='recent_post';"></div>
			<div class="span7">
				<section class="posts" ng-controller="PostListCtrl">
					<header>
						<h1>Status Update</h1>
					</header>

					<div class="page-post-form" >
						<?php $this->renderPartial('_form_post_message', array(
							'userId' => user()->getId(),
							'isJoined' => PageHelper::isJoined(user()->getId(), $slug),
						)); ?>
					</div>

					<div class="span5">
						<?php $this->renderPartial('pages/post-list', array(
							'userId' => user()->getId(),
							'isJoined' => PageHelper::isJoined(user()->getId(), $slug),
						)); ?>
					</div>
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
				<?php $this->renderPartial('/item/sidebar'); ?>			
			</div>
		</div>	
</div>