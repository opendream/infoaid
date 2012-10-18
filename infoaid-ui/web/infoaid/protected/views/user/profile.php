<div id="profile-<?php echo $userId; ?>" class="user-profile">
		<header class="info">
			<div>
			<?php $this->renderPartial('/page/sidebar', array(
				'userId' => user()->getId()						
			)); ?>
			</div>
			<div>
				<?php $this->renderPartial('header_profile', array('userId'=>$userId)); ?>
			</div>
		</header>
		
		
</div>