<?php

class FrontController extends IAController
{
	public function actionIndex()
	{
		$this->actionMap();
	}

	public function actionMap()
	{
		$this->render('index');
	}

}