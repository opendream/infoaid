<?php

class ApiController extends IAController
{
	public $jquery = FALSE;

	public $angular = FALSE;

	public function actions()
	{
		return array(
			'page'=>'application.controllers.api.PageAPIAction',
			'post'=>'application.controllers.api.PostAPIAction'
		);
	}

	public function actionFrontPageInfo()
	{
		$this->renderJSON(API::get('front_page/info'));
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

	public function actionComment($userId, $postId, $message) {
		$params = array('userId'=>$userId, 'postId'=>$postId, 'message'=>$message);
		$resultJson = API::post('post/comment', $params, 'json');
		$this->renderJSON($resultJson);
	}

	public function actionDeleteComment($commentId, $userId) {
		$resultJson = API::getJSON('comment/'.$commentId.'/delete', array('userId'=>$userId));
		$this->renderJSON($resultJson);
	}
}