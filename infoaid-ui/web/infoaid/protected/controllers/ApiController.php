<?php

class ApiController extends IAController
{
	public $jquery = FALSE;

	public $angular = FALSE;

	public function actions()
	{
		return array(
			'page'=>'application.controllers.api.PageAPIAction',
			'post'=>'application.controllers.api.PostAPIAction',
			'item'=>'application.controllers.api.ItemAPIAction',
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
			/*foreach($resultSearch->pages as $el) {
				$resultJson[] = array(
					'body' => $this->renderPartial('/page/header', array('slug'=>$el->slug, 'id'=>$el->id), true)
				);
			}
			*/
			$resultJson = $resultSearch->pages;
		}
		$this->renderJSON($resultJson);
	}

	public function actionMembers($slug) {
		$result = API::getJSON('page/'.urlencode($slug).'/top_members');
		foreach ($result->topMembers as &$member) {
			UserHelper::assignDefaultAvatar($member);
		}
		$this->renderJSON($result);
	}

	public function actionComment($postId, $message) {
		$params = array('postId'=>$postId, 'message'=>$message);
		$resultJson = API::post('post/comment', $params, 'json');
		$this->renderJSON($resultJson);
	}

	public function actionDeleteComment($commentId) {
		$resultJson = API::getJSON('comment/'.$commentId.'/delete');
		$this->renderJSON($resultJson);
	}

	public function actionPostMessage($slug, $message, $picOriginal, $picSmall) {
		$params = array('message'=>$message, 'picOriginal'=>$picOriginal, 'picSmall'=>$picSmall);
		$resultJson = API::post('page/'.urlencode($slug).'/post_message/', $params, 'json');
		$this->renderJSON($resultJson);
	}

	public function actionDeletePost($postId) {
		$resultJson = API::getJSON('post/'.$postId.'/delete');
		$this->renderJSON($resultJson);
	}

	public function actionItems() {
		$resultJson = API::getJSON('item/allItems');
		$this->renderJSON($resultJson);
	}

	public function actionPostNeed($slug, $itemId, $quantity) {
		$params = array('itemId'=>$itemId, 'quantity'=>$quantity);
		$resultJson = API::post('page/'.urlencode($slug).'/post_need/', $params, 'json');
		$this->renderJSON($resultJson);
	}

	public function actionPostResource($slug, $itemId, $quantity) {
		$params = array('itemId'=>$itemId, 'quantity'=>$quantity);
		$resultJson = API::post('page/'.urlencode($slug).'/post_resource', $params, 'json');
		$this->renderJSON($resultJson);

	}

	public function actionCreateItem($newItem) {
		$params = array('name'=>$newItem);
		$resultJson = API::post('item/createItem', $params, 'json');
		$this->renderJSON($resultJson);
	}

	public function actionGetPages() {
		$resultJson = API::getJSON('user/getPages');
		$this->renderJSON($resultJson);
	}
}