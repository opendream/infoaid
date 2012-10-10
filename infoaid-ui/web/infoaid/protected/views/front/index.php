<div class="flash-message-success">
		<?php
			if( Yii::app()->user->hasFlash('createSuccess') ) {
		?>
		<span class="alert alert-success">
			<?php
					echo Yii::app()->user->getFlash('createSuccess');
				}
			?>
		</span>
</div>
<div style="width:100%; height:400px" id="map"></div>