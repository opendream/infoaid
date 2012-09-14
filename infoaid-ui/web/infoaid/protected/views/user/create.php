<div id='user-create'>
	<header class="create">
		<div class='user-create-header'><span>Register</span></div>
	</header>
	<div class="flash-message-error">
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
	<div id='form-create'>
		<form class="form-horizontal" method='post' action='doCreate'>
			<div class="control-group">
			    <label class="control-label" for="input-username">Username</label>
			    <div class="controls">
			      <input type="text" id="input-username" value="<?php echo $username; ?>" name='username' placeholder="Username">
			    </div>
			</div>

			<div class="control-group">
			    <label class="control-label" for="input-password">Password</label>
			    <div class="controls">
			      <input type="password" id="input-password" name='password' placeholder="Password">
			    </div>
			</div>

			<div class="control-group">
			    <label class="control-label" for="input-re-password">Repeat Password</label>
			    <div class="controls">
			      <input type="password" id="input-re-password" name='re-password' placeholder="Repeat Password">
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
			      <button type="submit" class="btn">Register</button>
			    </div>
			</div>
			
		</form>
	</div>
</div>