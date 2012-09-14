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
		$slug = urlencode($slug);
		$result = API::getJSON('page/'. $slug .'/'. $method);
		if ($result->status == 1) {
			$posts = $result->posts;
		}
		$this->renderJSON($posts);
	}

	public function actionPostGetComment($id)
	{
		$_list = array();
		$result = API::getJSON('post/'. $id .'/comment');
		if ($result->status == 1) {
			$comments = $result->comments;
		}
		$this->renderJSON($comments);
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
}