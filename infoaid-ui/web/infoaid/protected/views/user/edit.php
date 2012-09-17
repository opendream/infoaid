<?php
	$session = new CHttpSession;
	$session->open();
?>
<div id='user-edit'>
	<header class="edit">
		<div class='user-edit-header'><span>Edit Profile</span></div>
	</header>
	<div class="flash-message">
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
	<div id='form-edit'>
		<form class="form-horizontal" method='post' action='doEdit'>
			<div class="control-group">
			    <label class="control-label" for="input-username">Username</label>
			    <div class="controls">
			      <label id="username"><?php echo $session['username']; ?></label>
			    </div>
			</div>

			<div class="control-group">
			    <div id='pic-original'></div>
			    <div class="controls">
			      <input type="file" id="input-pic-original" name='pic-original'>
			    </div>
			</div>

			<div class="control-group">
			    <label class="control-label" for="input-firstname">Firstname</label>
			    <div class="controls">
			      <input type="text" id="input-firstname" value="<?php echo $firstname; ?>" name='firstname' placeholder="Firstname">
			    </div>
			</div>

			<div class="control-group">
			    <label class="control-label" for="input-lastname">Lastname</label>
			    <div class="controls">
			      <input type="text" id="input-lastname" value="<?php echo $lastname; ?>" name='lastname' placeholder="Lastname">
			    </div>
			</div>

			<div class="control-group">
			    <label class="control-label" for="input-email">Email</label>
			    <div class="controls">
			      <input type="text" id="input-email" value="<?php echo $email; ?>" name='email' placeholder="Email">
			    </div>
			</div>

			<div class="control-group">
			    <label class="control-label" for="input-tel">Tel No.</label>
			    <div class="controls">
			      <input type="text" id="input-tel" value="<?php echo $tel; ?>" name='tel' placeholder="Tel No.">
			    </div>
			</div>

			<div class="control-group">
			    <div class="controls">
			      <button type="submit" class="btn">Edit</button>
			    </div>
			</div>
			
		</form>
	</div>
</div>
<?php
	$session->close();
?>