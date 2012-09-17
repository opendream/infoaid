<?php
	$session = new CHttpSession;
	$session->open();
?>
<div id='user-edit-password' ng-app>
	<header class="edit-password">
		<div class='user-edit-password-header'><span>Edit Password</span></div>
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
		<?php
			if( Yii::app()->user->hasFlash('editSuccess') ) {
		?>
		<span class="alert alert-error">
			<?php
					echo Yii::app()->user->getFlash('editSuccess');
				}
			?>
		</span>
	</div>
	<div id='form-edit-password' ng-controller="editPasswordController">
		<form class="form-horizontal" method='post' action='doEditPassword'>

			<div class="control-group" id="old-password-div">
			    <label class="control-label" for="input-old-password">Old Password</label>
			    <div class="controls">
			      <input type="password" id="input-old-password" name='old-password' placeholder="Old Password" ng-model="oldPassword" ng-change="change()">
			    </div>
			</div>

			<div class="control-group" id="new-password-div">
			    <label class="control-label" for="input-new-password">New Password</label>
			    <div class="controls">
			      <input type="password" id="input-new-password" name='new-password' placeholder="New Password" ng-model="newPassword" ng-change="change()">
			    </div>
			</div>

			<div class="control-group" id="confirm-password-div">
			    <label class="control-label" for="input-confirm-new-password">Confirm New Password</label>
			    <div class="controls">
			      <input type="password" id="input-confirm-new-password" name='confirm-new-password' placeholder="Confirm New Password" ng-model="confirmPassword" ng-change="change()">
			    </div>
			</div>

			<div class="control-group">
			    <div class="controls">
			      <button type="submit" class="btn" ng-click="submit()">Edit</button>
			    </div>
			</div>
			
		</form>
	</div>
</div>
<script>
	function editPasswordController($scope) {
		$scope.oldPassword = '';
		$scope.newPassword = '';
		$scope.confirmPassword = '';
		$scope.change = function() {
			if($scope.oldPassword == '') {
				$('#old-password-div').addClass("error");
				$('#input-old-password').tooltip({'title':"Please type your old password."}).tooltip('show');
				return false;
			} else {
				$('#old-password-div').removeClass("error");
				$('#input-old-password').tooltip({'title':"Please type your old password."}).tooltip('hide');
			}

			if($scope.newPassword == '') {
				$('#new-password-div').addClass("error");
				$('#input-new-password').tooltip({'title':"Please type your new password."}).tooltip('show');
				return false;
			} else {
				$('#new-password-div').removeClass("error");
				$('#input-new-password').tooltip({'title':"Please type your new password."}).tooltip('hide');
			}

			if($scope.confirmPassword == '') {
				$('#confirm-password-div').addClass("error");
				$('#input-confirm-new-password').tooltip({'title':"Please type your confirm password."}).tooltip('show');
				return false;
			} else {
				$('#confirm-password-div').removeClass("error");
				$('#input-confirm-new-password').tooltip({'title':"Please type your confirm password."}).tooltip('hide');
			}

			if($scope.newPassword != $scope.confirmPassword) {
				$('#new-password-div').addClass("error");
				$('#confirm-password-div').addClass("error");
				$('#input-confirm-new-password').tooltip({'title':"New password and Confirm password doesn't match"}).tooltip('show');
				return false;
			} else{
				$('#new-password-div').removeClass("error");
				$('#confirm-password-div').removeClass("error");
				$('#input-confirm-new-password').tooltip({'title':"New password and Confirm password doesn't match"}).tooltip('hide');
			}
			return true;
		}

		$scope.submit = function() {
			$('#form-edit-password').submit(function() {
				return $scope.change();
			});
		}
	}
</script>
<?php
	$session->close();
?>