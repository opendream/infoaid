<?php

class UserController extends IAController
{
	public $jquery = TRUE;

	public $angular = TRUE;

	public function actionCreate()
	{
		$this->render('create');
	}

	public function actionDoCreate()
	{
		$password = $_POST['password'];
		$rePassword = $_POST['re-password'];
		
		if($password != $rePassword) {
			Yii::app()->user->setFlash('error', "Please type same password and repeat password");
			$this->render('create', array(
				'username'=>$_POST['username'],
				'password'=>$_POST['password'],
				'firstname'=>$_POST['firstname'],
				'lastname'=>$_POST['lastname'],
				'email'=>$_POST['email'],
				'tel'=>$_POST['tel'],
				)
			);
			Yii::app()->end();
		}

		$resultCreate = API::getJSON('user/create', array(
			'username'=>$_POST['username'],
			'password'=>$_POST['password'],
			'firstname'=>$_POST['firstname'],
			'lastname'=>$_POST['lastname'],
			'email'=>$_POST['email'],
			'telNo'=>$_POST['tel'],
			)
		);
		if($resultCreate->id != null) {
			Yii::app()->user->setFlash('createSuccess', 'Register Success');
			$this->redirect(array('front/index'));
		} else {
			Yii::app()->user->setFlash('error', $resultCreate->message);
			$this->render('create', array(
				'username'=>$_POST['username'],
				'password'=>$_POST['password'],
				'firstname'=>$_POST['firstname'],
				'lastname'=>$_POST['lastname'],
				'email'=>$_POST['email'],
				'tel'=>$_POST['tel'],
				)
			);
			Yii::app()->end();
		}
	}

	public function actionEdit()
	{
		$session = new CHttpSession;
		$session->open();

		$userId = $session['userId'];

		$session->close();

		$resultGetBasicInfo = API::getJSON('user/showBasicInfo', array(
			'id'=>$userId
			)
		);

		if($resultGetBasicInfo->id == null) {
			
		}
		$this->render('edit', array(
			'id'=>$resultGetBasicInfo->id,
			'username'=>$resultGetBasicInfo->username,
			'firstname'=>$resultGetBasicInfo->firstname,
			'lastname'=>$resultGetBasicInfo->lastname,
			'email'=>$resultGetBasicInfo->email,
			'tel'=>$resultGetBasicInfo->telNo,
			'pic-original'=>$resultGetBasicInfo->picOriginal
			)
		);
	}

	public function actionDoEdit()
	{
		
	}

	public function actionEditPassword()
	{
		$this->render('editPassword');
	}

	public function actionDoEditPassword()
	{
		$session = new CHttpSession;
		$session->open();

		$userId = $session['userId'];

		$session->close();

		$oldPassword = $_POST['old-password'];
		if($oldPassword == '') {
			Yii::app()->user->setFlash('error', "Please type your old password.");
			$this->render('editPassword');
			Yii::app()->end();
		}

		$newPassword = $_POST['new-password'];
		$confirmPassword = $_POST['confirm-new-password'];

		if($newPassword == '' || $confirmPassword == '') {
			Yii::app()->user->setFlash('error', "Please type your new password and confirm password");
			$this->render('editPassword');
			Yii::app()->end();
		}else if($newPassword != $confirmPassword) {
			Yii::app()->user->setFlash('error', "New password and Confirm new password doesn't match.");
			$this->render('editPassword');
			Yii::app()->end();
		} else {
			$resultEditPassword = API::getJSON('user/updatePassword', array(
				'id'=>$userId,
				'oldPassword'=>$oldPassword,
				'newPassword'=>$newPassword,
				'confirmedPassword'=>$confirmPassword
				)
			);
		}

		if($resultEditPassword->message == 'password is updated') {
			Yii::app()->user->setFlash('editSuccess', 'Password is Updated');
			$this->redirect(array('user/editPassword'));
		} else {
			Yii::app()->user->setFlash('error', $resultEditPassword->message);
			$this->render('editPassword');
			Yii::app()->end();
		}
	}
}