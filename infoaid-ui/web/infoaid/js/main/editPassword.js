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
		$('#form-edit').submit(function() {
			return $scope.change();
		});
	}
}