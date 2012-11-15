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

			<?php $this->renderPartial('_form_account', array('user' => $user)); ?>
			
			<?php $this->renderPartial('_form_personal', array('user' => $user)); ?>

			<?php $this->renderPartial('_form_expertise', array('user' => $user)); ?>

			<?php $this->renderPartial('_form_cause', array('user' => $user)); ?>

			<div class="form-actions">
				<button type="submit" class="btn">Register</button>
			</div>
			
		</form>
	</div>
</div>