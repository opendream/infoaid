<?php /* @var $this Controller */ ?>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="language" content="en" />

	<!-- blueprint CSS framework -->
	<link rel="stylesheet" type="text/css" href="<?php echo Yii::app()->request->baseUrl; ?>/css/screen.css" media="screen, projection" />
	<link rel="stylesheet" type="text/css" href="<?php echo Yii::app()->request->baseUrl; ?>/css/print.css" media="print" />
	<!--[if lt IE 8]>
	<link rel="stylesheet" type="text/css" href="<?php echo Yii::app()->request->baseUrl; ?>/css/ie.css" media="screen, projection" />
	<![endif]-->

	<link rel="stylesheet" type="text/css" href="<?php echo Yii::app()->request->baseUrl; ?>/css/main.css" />
	<link rel="stylesheet" type="text/css" href="<?php echo Yii::app()->request->baseUrl; ?>/css/form.css" />
	<link rel="stylesheet" type="text/css" href="<?php echo Yii::app()->request->baseUrl; ?>/css/bootstrap/css/bootstrap.min.css" />
	
	<script>
		var baseUrl = '<?php echo Yii::app()->baseUrl; ?>';
	</script>
	<title><?php echo CHtml::encode($this->pageTitle); ?></title>
</head>

<body>

<div class="container" id="page">
	<div class="navbar navbar-static-top">
		<div class="navbar-inner">            
			<a class="brand" href="<?php echo Yii::app()->homeUrl ?>"><?php echo CHtml::encode(Yii::app()->name); ?></a>

			<?php $this->widget('zii.widgets.CMenu', array(
				'htmlOptions' => array(
					'class' => 'nav actions',
				),
				'items' => array(
					array('label' => 'Place', 'url' => array('/page/search')),
					array('label' => 'People', 'url' => array('people')),
					array('label' => 'Status', 'url' => array('status')),
					array('label' => 'About us', 'url' => array('about')),
				),
			)); ?>

			<?php if (Yii::app()->user->isGuest): ?>

			<?php $this->widget('zii.widgets.CMenu', array(
				'htmlOptions' => array(
					'class' => 'nav menu pull-right',
				),
				'items' => array(
					array('label' => 'Log in', 'url' => array('/user/login')),
					array('label' => 'Register', 'url' => array('/user/create')),
				),
			)); ?>

			<?php else: ?>

			<?php $this->widget('zii.widgets.CMenu', array(
				'htmlOptions' => array(
					'class' => 'nav menu pull-right',
				),
				'encodeLabel' => false,
				'items' => array(
					array(
						'label' => '<i class="icon-user icon-black"></i> '. Yii::app()->user->getName() .' <b class="caret"></b>',
						'url' => array('user/login'),
						'linkOptions' => array(
							'class' => 'dropdown-toggle',
							'data-toggle' => 'dropdown',
						),
						'submenuOptions' => array(
							'class' => 'dropdown-menu',
						),
						'items' => array(
							array('label' => 'Edit Profile', 'url' => '/user/edit'),
							array('label' => 'Create Page', 'url' => '/page/create'),
							array(
								'template' => '<li class="divider"></li>',
							),
							array('label' => 'Logout', 'url' => '/user/logout'),
						),
					),
				),
			)); ?>
			
			<p class="navbar-text pull-right">Log in as </p>
			<?php endif; ?>
		</div> <!-- navbar inner -->
	</div>
	
	<?php if(isset($this->breadcrumbs)):?>
		<?php $this->widget('zii.widgets.CBreadcrumbs', array(
			'links'=>$this->breadcrumbs,
		)); ?><!-- breadcrumbs -->
	<?php endif?>

	<?php echo $content; ?>

	<div class="clear"></div>

	<div id="footer">
		Copyright &copy; <?php echo date('Y'); ?> by Opendream Co., Ltd.<br/>
		All Rights Reserved.<br/>		
	</div><!-- footer -->

</div><!-- page -->

</body>
</html>
