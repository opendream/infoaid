<?php
	$user = UserHelper::basicInfo($userId);
?>
<div id="user-header-<?php echo $userId; ?>" class="page-header profile-header span10">
	<div id="user-header-left-<?php echo $userId; ?>">
		<div id="user-header-left-pic-<?php echo $userId; ?>" class="user-header-picture">
			<?php 
                if($user->picOriginal == null) {
                    echo '<a href="'.Yii::app()->baseUrl.'/user/profile/'.$userId.'"><img src='.Yii::app()->baseUrl.'/media/profiles/profile_default_large.png class="img-polaroid"></a>';
                } else {
                    echo '<a href="'.Yii::app()->baseUrl.'/user/profile/'.$userId.'"><img src='.Yii::app()->baseUrl . $user->picLarge . ' class="img-polaroid"></a>';
                }
            ?>
		</div>
	</div>
	<div id="user-header-right-<?php echo $userId; ?>">
		<div id="user-header-right-username-<?php echo $user->username?>">
			<h3>
				<?php
					echo '<a href="'.Yii::app()->baseUrl.'/user/profile/'.$userId.'">'.$user->username.'</a>';
				?>
			</h3>
			<p><span><b><?php echo $user->firstname; ?></span><span class="user-header-lastname"><?php echo $user->lastname; ?></b></span></p>
			<p><span><b>Email : </b><?php echo $user->email; ?></span></p>
			<p><span><b>Telephone : </b><?php echo $user->telNo; ?></span></p>
		</div>
	</div>
</div>