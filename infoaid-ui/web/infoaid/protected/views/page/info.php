<div id="page-<?php echo $page->id; ?>" class="page-info">

		<header class="info">
			<?php $this->renderPartial('header', array('slug'=>$slug,'id'=>$page->id)); ?>
		</header>
		
		<div class="row" ng-app="post">				
			<div ng-init="slug='<?php echo $slug ?>'"></div>
			<div>
				<?php $this->renderPartial('sidebar'); ?>
			</div>
			<div class="span8">
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
				</div>				
			</div>
		</div>	
</div>