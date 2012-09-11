<?php

class ApiController extends IAController
{
	public $jquery = FALSE;

	public $angular = FALSE;

	public function actionFrontPageInfo()
	{
		$this->renderJSON(API::get('front_page/info'));
	}

	public function actionPage($slug, $method)
	{
		$posts = array();
		$result = API::getJSON('page/'. $slug .'/'. $method);
		if ($result->status == 1) {
			$posts = $result->posts;
		}
		$this->renderJSON($posts);
	}
}