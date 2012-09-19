<fieldset class="form-group">

	<?php if (! $no_legend): ?>
		<legend>Account</legend>
	<?php endif; ?>

	<?php $this->renderPartial('_form_user_email'); ?>

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

</fieldset>

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