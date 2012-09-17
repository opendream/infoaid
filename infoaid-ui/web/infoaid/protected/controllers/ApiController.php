<?php

class ApiController extends IAController
{
	public $jquery = FALSE;

	public $angular = FALSE;

	public function actions()
	{
		return array(
			'page'=>'application.controllers.api.PageAPIAction',
		);
	}

	public function actionFrontPageInfo()
	{
		$this->renderJSON(API::get('front_page/info'));
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
		$resultJson = array();
		if(sizeOf($resultSearch->pages) == 0) {
		} else {
			foreach($resultSearch->pages as $el) {
				$resultJson[] = array(
					'body' => $this->renderPartial('/page/header', array('slug'=>$el->slug, 'id'=>$el->id), true)
				);
			}
		}
		$this->renderJSON($resultJson);
	}

	public function actionMembers($slug) {
		$resultNeeds = API::getJSON('page/'.urlencode($slug).'/top_members');
		$this->renderJSON($resultNeeds);
	}
}