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

	public function actionPageInfo($slug)
	{	
		$slug = urlencode($slug);
		$resultInfo = API::getJSON("page/$slug/info");
		$this->renderJSON($resultInfo);
	}

	public function actionPageNeeds($slug)
	{
		$slug = urlencode($slug);
		$resultNeeds = API::getJSON('page/'.$slug.'/limit_need/4');
		$this->renderJSON($resultNeeds);
	}
}