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
			      <input type="text" id="input-username" value="<?php echo $username; ?>" name='username' placeholder="Username" required>
			    </div>
			</div>

			<div class="control-group">
			    <label class="control-label" for="input-password">Password</label>
			    <div class="controls">
			      <input type="password" id="input-password" name='password' placeholder="Password" required>
			    </div>
			</div>

			<div class="control-group">
			    <label class="control-label" for="input-re-password">Repeat Password</label>
			    <div class="controls">
			      <input type="password" id="input-re-password" name='re-password' placeholder="Repeat Password" required>
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
			      <input type="email" id="input-email" value="<?php echo $email; ?>" name='email' placeholder="Email" required>
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

<script>
jQuery(function ($) {
	$('#input-password, #input-re-password').bind('input', function (event) {
		var
			p1 = $('#input-password'),
			p2 = $('#input-re-password')
		;

		if (p1.val() != p2.val()) {
			p2[0].setCustomValidity('The both password field must match');
		} else {
			// input is valid -- reset the error message
			p2[0].setCustomValidity('');
		}
	});
});
</script>