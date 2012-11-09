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

		<?php $this->renderPartial('/_flash'); ?>

		<div id='form-edit'>
			<?php if ($section == 'password'): ?>
				<form class="form-horizontal" method='POST' action='<?php echo $this->createUrl("user/doEdit/$section"); ?>' ng-controller="editPasswordController" ng-submit="submit()">
			<?php elseif ($section == 'photo'): ?>
				<form class="form-horizontal" method='POST' action='<?php echo $this->createUrl("user/doEdit/$section"); ?>' enctype="multipart/form-data">
			<?php else: ?>
				<form class="form-horizontal" method='POST' action='<?php echo $this->createUrl("user/doEdit/$section"); ?>'>
			<?php endif; ?>

				<?php csrf_token_form(); ?>


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
						'user' => $user,
						'no_legend' => true,
					)); ?>

				<?php elseif ($section == 'expertise'): ?>
					<?php $this->renderPartial('_form_expertise', array(
						'user' => $user,
						'no_legend' => true,
						'expertises' => Yii::app()->params['expertises'],
					)); ?>

				<?php endif; ?>


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