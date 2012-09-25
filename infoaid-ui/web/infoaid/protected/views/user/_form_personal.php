<fieldset class="form-group">

	<?php if (! $no_legend): ?>
		<legend>Personal Information</legend>
	<?php endif; ?>

	<div class="control-group">
		<label class="control-label" for="input-firstname">Firstname</label>
		<div class="controls">
		  <input type="text" id="input-firstname" value="<?php echo $user->firstname; ?>" name='firstname' placeholder="Firstname">
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" for="input-lastname">Lastname</label>
		<div class="controls">
		  <input type="text" id="input-lastname" value="<?php echo $user->lastname; ?>" name='lastname' placeholder="Lastname">
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" for="input-tel">Tel No.</label>
		<div class="controls">
		  <input type="text" id="input-tel" value="<?php echo $user->telNo; ?>" name='tel' placeholder="Tel No.">
		</div>
	</div>
</fieldset>