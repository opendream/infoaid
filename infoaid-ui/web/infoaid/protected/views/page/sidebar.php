<?php
    $resultPages = API::getJSON('user/getPages', array('id'=>8185));
    $resultUserInfo = API::getJSON('user/showBasicInfo', array('id'=>8185));
?>
<div id='page-sidebar' class='span2'>
	<div id='page-sidebar-profile'>
		<div class='page-sidebar-profile'>
			<span>
				<?php
					if($post->picSmall == null) {
		                echo '<img src='.Yii::app()->baseUrl.'/media/profiles/profile_default_small.png class="img-polaroid">';
		            } else {
		                echo '<img src=' . Yii::app()->baseUrl . $resultUserInfo->picSmall . ' class="img-polaroid">';
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
			<li class='left-side-bar li-feeds'><?php echo CHtml::link('New Feeds'); ?></li>
			<li class='left-side-bar li-private-message'><?php echo CHtml::link('Private Messages'); ?></li>

		</ul>
	</div>
	<div id='page-sidebar-places'>
		<div id='page-sidebar-places' class='page-sidebar-places-header'><p class='muted'>Place</p></div>
		<ul class='unstyled ul-list'>
		<?php 
			foreach($resultPages->pages as $el) {
		?>
				<li class='left-side-bar li-place'><?php echo CHtml::link($el->name, array("page/$el->slug")); ?></li>
		<?php
			}
		?>
			<li class='left-side-bar li-add-group'><?php echo CHtml::link('Add Group..'); ?></li>
		<ul>
	</div>
</div>