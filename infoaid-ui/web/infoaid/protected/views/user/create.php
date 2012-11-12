<div id='user-create'>
	<header class="create page-header">
		<h1>Register</h1>
	</header>

	<div class="flash-message-error">
			<?php
				if( Yii::app()->user->hasFlash('error') ) {
			?>
			<span class="alert alert-error">
				<?php
						echo Yii::app()->user->getFlash('error');
					}
				?>
			</span>
	</div>
	<div id='form-create'>
		<form class="form-horizontal" method='post' action='doCreate'
			enctype="multipart/form-data">

			<?php csrf_token_form(); ?>

			<?php $this->renderPartial('_form_account'); ?>
			
			<?php $this->renderPartial('_form_personal'); ?>

			<?php $this->renderPartial('_form_expertise'); ?>

			<div class="form-actions">
				<button type="submit" class="btn">Register</button>
			</div>
			
		</form>
	</div>
</div>