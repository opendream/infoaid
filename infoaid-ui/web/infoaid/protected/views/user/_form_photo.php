<fieldset class="form-group">

	<?php if (! $no_legend): ?>
		<legend>Profile Photo</legend>
	<?php endif; ?>

	<div class="control-group">
		<label class="control-label" for="input-image">Photo</label>
		<div class="controls">
			<?php if ($user->picLarge): ?>
				<img class="img-polaroid" src="<?php echo Yii::app()->baseUrl . $user->picLarge; ?>" />
			<?php endif; ?>
			<input type="file" id="input-image" name="image">
		</div>
	</div>
</fieldset>