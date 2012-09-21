<div id='user-edit' class="container-fluid user-edit-wrapper" ng-app>
<div class="row-fluid">

	<div class="span3 well">
		<?php $this->renderPartial('_nav', array(
			'baseUrl' => Yii::app()->baseUrl .'/user/edit/',
			'section_map' => $section_map,
			'section' => $section,
		)); ?>
	</div>

	<div class="content-main span9">
		<header class="page-header">
			<h1>Edit <?php echo $section_name; ?></h1>
		</header>

		<div class="flash-message">
			<?php if( Yii::app()->user->hasFlash('error') ): ?>
				<div class="alert alert-error">
					<?php echo Yii::app()->user->getFlash('error'); ?>
				</div>
			<?php endif; ?>

			<?php if( Yii::app()->user->hasFlash('success') ): ?>
				<div class="alert alert-success">
					<?php echo Yii::app()->user->getFlash('success'); ?>
				</div>
			<?php endif; ?>
		</div>

		<div id='form-edit'>
			<?php if ($section == 'password'): ?>
				<form class="form-horizontal" method='POST' action='<?php echo $this->createUrl("user/doEdit/$section"); ?>' ng-controller="editPasswordController" ng-submit="submit()">
			<?php else: ?>
				<form class="form-horizontal" method='POST' action='<?php echo $this->createUrl("user/doEdit/$section"); ?>'>
			<?php endif; ?>


				<input type="hidden" name="section" value="<?php echo $section; ?>" />

				<?php if ($section == 'account'): ?>
					<?php $this->renderPartial('_form_user_email', array(
						'user' => $user,
					)); ?>

					<?php $this->renderPartial('_form_personal', array(
						'user' => $user,
					)); ?>

				<?php elseif ($section == 'password'): ?>
					<?php $this->renderPartial('_form_edit_password'); ?>

				<?php elseif ($section == 'photo'): ?>
					<?php $this->renderPartial('_form_photo', array(
						'no_legend' => true,
					)); ?>

				<?php endif; ?>

				<hr />

				<div class="control-group">
				    <div class="controls">
				      <button type="submit" class="btn">Save</button>
				    </div>
				</div>
				
			</form>
		</div>
	</div>

</div>
</div>