<?php

class UserController extends IAController
{
	public $jquery = TRUE;

	public $angular = TRUE;

	public $styles = array(
		'user.scss',
	);

	public function actionCreate()
	{
		$this->render('create');
	}

	public function actionDoCreate()
	{
		$password = $_POST['password'];
		$rePassword = $_POST['re-password'];
		$passwordLength = strlen($password);
		
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
		} else if($passwordLength < 7 || $passwordLength > 20) {
			Yii::app()->user->setFlash('error', "Password must have 7 to 20 character");
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

	public function actionEdit($section='account')
	{
		$this->scripts[] = 'main/editPassword.js';

		$resultGetBasicInfo = API::getJSON('user/showBasicInfo', array(
			'id' => Yii::app()->user->getId()
		));

		if($resultGetBasicInfo->id == null) {
			
		}

		$section_map = array(
			'account' => 'Account',
			'personal' => 'Personal Information',
			'password' => 'Password',
			'photo' => 'Profile Photo',
		);

		$this->render('edit', array(
			'section'=>$section,
			'section_name'=>$section_map[$section],
			'section_map'=>$section_map,
			'id'=>$resultGetBasicInfo->id,
			'username'=>$resultGetBasicInfo->username,
			'firstname'=>$resultGetBasicInfo->firstname,
			'lastname'=>$resultGetBasicInfo->lastname,
			'email'=>$resultGetBasicInfo->email,
			'tel'=>$resultGetBasicInfo->telNo,
			'pic-original'=>$resultGetBasicInfo->picOriginal
		));
	}

	public function actionDoEdit($section='account')
	{
		switch ($section) {
			case 'password':
				$this->actionDoEditPassword();
				break;
		}
	}

	public function actionDoEditPassword()
	{
		$editPasswordUrl = $this->createUrl("user/edit/password");

		$oldPassword = $_POST['old-password'];
		if($oldPassword == '') {
			Yii::app()->user->setFlash('error', "Please type your old password.");
			
			$this->redirect($editPasswordUrl);
			Yii::app()->end();
		}

		$newPassword = $_POST['new-password'];
		$confirmPassword = $_POST['confirm-new-password'];

		if ($newPassword == '' || $confirmPassword == '') {
			Yii::app()->user->setFlash('error', "Please type your new password and confirm password");

			$this->redirect($editPasswordUrl);
			Yii::app()->end();
		}
		else if ($newPassword != $confirmPassword) {
			Yii::app()->user->setFlash('error', "New password and Confirm new password doesn't match.");

			$this->redirect($editPasswordUrl);
			Yii::app()->end();
		}
		else {
			$resultEditPassword = API::getJSON('user/updatePassword', array(
				'id'=>Yii::app()->user->getId(),
				'oldPassword'=>$oldPassword,
				'newPassword'=>$newPassword,
				'confirmedPassword'=>$confirmPassword
			));
		}

		if ($resultEditPassword->message == 'password is updated') {
			Yii::app()->user->setFlash('success', 'Password is Updated');

			UserHelper::updateCachedPassword(Yii::app()->user->getId(), Yii::app()->user->getName(), $newPassword);
		}
		else {
			$message = $resultEditPassword->message ?: "There's something wrong, please try again";
			Yii::app()->user->setFlash('error', $message);
		}

		$this->redirect($editPasswordUrl);
	}

	public function actionLogin()
	{
		if (! empty($_POST)) {
			$username = trim($_POST['username']);
			$password = $_POST['password'];

			if ($username && mb_strlen($password) > 0) {
				$identity = new IAUserIdentity($username, $password);

				$isAuthorized = $identity->authenticate();

				if ($isAuthorized) {
					Yii::app()->user->login($identity, 60*60*24);
					$this->redirect(Yii::app()->user->returnUrl);
				}
				else {
					Yii::app()->user->setFlash('error', 'User/Password is not corrected');
				}
			}
		}

		$this->render('login');
	}

	public function actionLogout()
	{
		Yii::app()->user->logout();
		$this->redirect(Yii::app()->homeUrl);
	}
}