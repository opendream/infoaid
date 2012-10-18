<?php
	if($userId != null) {
    	//$resultPages = API::getJSON('user/getPages', array('id'=>$userId));
    	$resultUserInfo = API::getJSON("user/$userId/basic_info");
	}    
?>
<div id='page-sidebar' class='page-sidebar' ng-app='headerService'>
	<div id='page-sidebar-joinus'>
		<?php
			if($userId == null) {
		?>
			<div class='page-sidebar-joinus'>
				<?php 
				echo CHtml::link('Register',array('user/create'),array('class'=>'btn-info btn-large btn-joinus'));
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
						if($resultUserInfo->picSmall == null) {
			                echo '<a href="'.Yii::app()->baseUrl.'/user/profile/'.$resultUserInfo->id.'"><img src='.Yii::app()->baseUrl.'/media/profiles/profile_default_small.png class="img-polaroid"></a>';
			            } else {
			                echo '<a href="'.Yii::app()->baseUrl.'/user/profile/'.$resultUserInfo->id.'"><img src=' . Yii::app()->baseUrl . $resultUserInfo->picSmall . ' class="img-polaroid"></a>';
			            }
		        	}
				?>
			</span>
			<span class='page-sidebar-profile-username'>
				<?php
	                echo CHtml::link($resultUserInfo->username, array("user/profile/$resultUserInfo->id"));
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
	<div id='page-sidebar-places' ng-controller="SidebarCtrl">
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
			<li class='left-side-bar li-place' ng-repeat="page in pages">
				<a href="<?php echo Yii::app()->baseUrl."/page/{{page.slug}}";?>">
					{{page.name}}
				</a>
			</li>
		
			<li class='left-side-bar li-add-group'>
				<?php
					if($userId != null) { 
						echo CHtml::link('Add Group..', array("page/create"));
					}
				?>
			</li>
		<ul>
	</div>
</div>