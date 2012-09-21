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

		$userId = Yii::app()->user->getId();

		$info = UserHelper::basicInfo($userId);

		if(! $info->id) {
			//throw new CHttpException(404, 'The specified account cannot be found.');
		}

		$section_map = array(
			'account' => 'Account',
			'password' => 'Password',
			'photo' => 'Profile Photo',
		);

		$this->render('edit', array(
			'section'=>$section,
			'section_name'=>$section_map[$section],
			'section_map'=>$section_map,
			'user'=>$info,
		));
	}

	public function actionDoEdit($section='account')
	{
		$userId = Yii::app()->user->getId();

		$info = UserHelper::basicInfo($userId);

		if(! $info->id) {
			throw new CHttpException(404, 'The specified account cannot be found.');
		}

		switch ($section) {
			case 'account':
				$this->actionDoEditAccount($info);
				break;
			case 'password':
				$this->actionDoEditPassword($info);
				break;
			case 'photo':
				$this->actionDoEditPhoto($info);
				break;
		}
	}

	public function actionDoEditAccount($info)
	{
		$userId = Yii::app()->user->getId();
		$editAccountUrl = $this->createUrl("user/edit/account");

		$username  = $_POST['username'];
		$email     = $_POST['email'];
		$firstname = $_POST['firstname'];
		$lastname  = $_POST['lastname'];
		$telNo     = $_POST['tel'];

		if ($username == '') {
			Yii::app()->user->setFlash('error', "Username cannot be blank");
		}

		if ($email == '') {
			Yii::app()->user->setFlash('error', "Email cannot be blank");
		}

		$data = array(
			'userId' => $userId,
			'username' => $username,
			'email' => $email,
			'firstname' => $firstname,
			'lastname' => $lastname,
			'telNo' => $telNo,
			'picOriginal' => $info->picOriginal,
		);

		$result = UserHelper::updateBasicInfo($userId, $data);
		if ($result) {
			Yii::app()->user->setFlash('success', 'Update account success.');
			$this->redirect($editAccountUrl);
		}
		else {
			Yii::app()->user->setFlash('error', $result->message ?: "There's something wrong, please try again");
			$this->redirect($editAccountUrl);
		}
	}

	public function actionDoEditPassword($info)
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

	public function actionDoEditPhoto($info)
	{
		$editPhotoUrl = $this->createUrl('/user/edit/photo');

		$photos = UserHelper::processUploadedProfilePhoto($info->id, 'image');
		if (is_array($photos)) {
			$info->picOriginal = $photos['original']['url'];
			$info->picLarge = $photos['large']['url'];
			$info->picSmall = $photos['small']['url'];
			// TODO: use $id or $userId, to be adjusted.
			$info->userId = $info->id;

			$result = UserHelper::updateBasicInfo($userId, (array)$info);
			if ($result) {
				Yii::app()->user->setFlash('success', 'Update photo success.');
			}
			else {
				Yii::app()->user->setFlash('error', $result->message ?: "There's something wrong, please try again");
			}
		}
		else {
			Yii::app()->user->setFlash('error', $photos);
		}
		
		$this->redirect($editPhotoUrl);
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