<div id="profile-<?php echo $userId; ?>" class="user-profile"><div class="row">
	<div class="span3">
		<?php $this->renderPartial('/page/sidebar', array(
			'userId' => user()->getId()						
		)); ?>
	</div>

	<div class="span9">
		<header class="info">
			<div>
				<?php $this->renderPartial('header_profile', array('userId'=>$userId)); ?>
			</div>
		</header>

		<section class="expertise">
			<h3>Area of Expertises</h3>

			<ul class="unstyled user-expertises">
				<?php foreach ($user->expertises as $expertise): ?>
				<li class="span2">
					<div class="expertise-name">
						<?php echo $expertise->name; ?>
					</div>
					<div class="expertise-description hide">
						<?php echo $expertise->description; ?>
					</div>
				</li>
				<?php endforeach; ?>
			</ul>

			<div class="clear"></div>
		</section>

		<section class="cause">
			<h3>Causes</h3>

			<ul class="unstyled user-causes">
				<?php foreach ($user->causes as $cause): ?>
				<li class="span2">
					<div class="expertise-name">
						<?php echo $cause->name; ?>
					</div>
					<div class="expertise-description hide">
						<?php echo $cause->description; ?>
					</div>
				</li>
				<?php endforeach; ?>
			</ul>

			<div class="clear"></div>
		</section>
	</div>
		
</div></div>