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
	<link rel="stylesheet" type="text/css" href="<?php echo Yii::app()->request->baseUrl; ?>/css/select2/select2.css" />

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

			<ul class="nav actions">
				<li><a href="#">Place</a></li>
				<li><a href="#">People</a></li>
				<li><a href="#">Status</a></li>
				<li><a href="/site/pages/about">About us</a></li>
			</ul>

			<?php if (Yii::app()->user->isGuest): ?>
			<ul class="nav menu pull-right">
					<li>
						<a href="<?php echo $this->createUrl('/user/login'); ?>">
							Log in
						</a>
					</li>
					<li>
						<a href="<?php echo $this->createUrl('/user/register'); ?>">
							Register
						</a>
					</li>
			</ul>
			<?php else: ?>
			<ul class="nav menu pull-right">
				<li class="dropdown">
					<a href="" class="dropdown-toggle" data-toggle="dropdown"> <i class="icon-user icon-black"></i> <?php echo Yii::app()->user->getName(); ?> <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li><a href="<?php echo $this->createUrl('user/edit'); ?>">Edit Profile</a></li>                                
						<li class="divider"></li>
						<li><a href="<?php echo $this->createUrl('user/logout'); ?>">Logout</a></li>
					</ul>
				</li>
			</ul>
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
