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

			<?php $this->renderPartial('_form_account'); ?>
			
			<?php $this->renderPartial('_form_personal'); ?>

			<div class="form-actions">
				<button type="submit" class="btn">Register</button>
			</div>
			
		</form>
	</div>
</div>