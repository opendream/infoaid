<?php
	$session = new CHttpSession;
	$session->open();
	$userId = 8185;
	if($userId != null) {
    	$resultPages = API::getJSON('user/getPages', array('id'=>$userId));
    	$resultUserInfo = API::getJSON("user/$userId/basic_info");
	}
    $session->close();
?>
<div id='page-sidebar' class='page-sidebar'>
	<div id='page-sidebar-joinus'>
		<?php
			if($userId == null) {
		?>
			<div class='page-sidebar-joinus'>
				<?php 
				echo CHtml::link('Join Us',array('user/create'),array('class'=>'btn-info btn-large btn-joinus'));
				?>
			</div>
		<?php
			}
		?>
	</div>
	<div id='page-sidebar-profile'>
		<div class='page-sidebar-profile'>
			<span>

				<?php
					if($userId != null) {
						if($post->picSmall == null) {
			                echo '<img src='.Yii::app()->baseUrl.'/media/profiles/profile_default_small.png class="img-polaroid">';
			            } else {
			                echo '<img src=' . Yii::app()->baseUrl . $resultUserInfo->picSmall . ' class="img-polaroid">';
			            }
		        	}
				?>
			</span>
			<span class='page-sidebar-profile-username'>
				<?php
	                echo CHtml::link($resultUserInfo->username);
				?>
			</span>
		</div>
	</div>
	<div id='page-sidebar-menu' class='page-sidebar-menu'>
		<ul class='unstyled ul-list'>
			<li class='left-side-bar li-feeds'>
				<?php 
					if($userId != null) {
						echo CHtml::link('New Feeds'); 
					}
				?>
			</li>
			<li class='left-side-bar li-private-message'>
				<?php 
					if($userId != null) {
						echo CHtml::link('Private Messages'); 
					}
				?>
			</li>

		</ul>
	</div>
	<div id='page-sidebar-places'>
		<div id='page-sidebar-places' class='page-sidebar-places-header'><p class='muted'>
			<?php
				if($userId != null) {
			?>
			Place
			<?php
				}
			?>
			</p></div>
		<ul class='unstyled ul-list'>
		<?php 
			if($userId != null) {
				foreach($resultPages->pages as $el) {
		?>
					<li class='left-side-bar li-place'><?php echo CHtml::link($el->name, array("page/$el->slug")); ?></li>
		<?php
				}
			}
		?>
			<li class='left-side-bar li-add-group'>
				<?php
					if($userId != null) { 
						echo CHtml::link('Add Group..');
					}
				?>
			</li>
		<ul>
	</div>
</div>