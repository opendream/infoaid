<div class="control-group">
	<label class="control-label" for="input-username">Username</label>
	<div class="controls">
	  <input type="text" id="input-username" value="<?php echo $user->username; ?>" name='username' placeholder="Username" required>
	</div>
</div>

<div class="control-group">
	<label class="control-label" for="input-email">Email</label>
	<div class="controls">
	  <input type="email" id="input-email" value="<?php echo $user->email; ?>" name='email' placeholder="Email" required>
	</div>
</div>