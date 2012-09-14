<?php

class UserController extends IAController
{
	public $jquery = FALSE;

	public $angular = FALSE;

	public function actionPageSearch($word, $offset)
	{
		$resultSearch = API::getJSON('page/searchPage', array('word'=>$word,'offset'=>$offset));
		foreach($resultSearch->pages as $el) {
			$resultJson[] = array(
				'body' => $this->renderPartial('/page/header', array('slug'=>$el->slug, 'id'=>$el->id), true),
			);
		}
		$this->renderJSON($resultJson);
	}

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
}