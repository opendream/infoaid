<?php /* @var $this Controller */ ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
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
	<link rel="stylesheet" type="text/css" href="<?php echo Yii::app()->request->baseUrl; ?>/css/bootstrap.min.css" />

	<script src="<?php echo Yii::app()->request->baseUrl; ?>/js/bootstrap.min.js"></script>
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
            <ul class="nav menu pull-right">
                <li class="dropdown" id="fat">            
	                <a href="#fat" class="dropdown-toggle" data-toggle="dropdown">Log in as <i class="icon-user icon-black"></i> admin <b class="caret"></b></a>
	                <ul class="dropdown-menu">
	                    <li><a href="/user/edit/1">Edit Profile</a></li>
	                    	<li><a href="/user/list">List All Users</a></li>
	                        <li class="divider"></li>
	                        <li><a href="/event/create">Create New Event</a></li>
	                        <li><a href="/user/create">Create New User</a></li>                                
	                    <li class="divider"></li>
	                    <li><a href="/logout/index">Logout</a></li>
	                </ul>
                </li>
            </ul>
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
