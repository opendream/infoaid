<?php

class PageController extends IAController
{

	public $styles = array(
		'page.scss',
	);

	public function actions()
	{
		return array(
			'static'=>array(
				'class'=>'IANoLayoutViewAction',
			),
		);
	}
	
	public function actionView($slug)
	{
       $this->scripts[] = 'main/postService.js';
       $this->scripts[] = 'main/commentService.js';
       $this->scripts[] = 'controllers.js';

		$result = PageHelper::getInfoBySlug($slug);
		if ($result) {
			$this->render('info', array(
				'page'=>$result,
				'slug'=>$slug,
			));
		}
		else {
			$this->renderText('Page not found');
		}
	}

	public function actionSearch()
	{
		$this->render('search');
	}

	public function actionMembers($slug)
	{
		$this->render('members');
	}

	public function actionLoadMoreMembers($slug, $offset)
	{
		$resultMembers = API::getJSON("page/$slug/members", array('offset'=>$offset));
		$resultJson = array();
		if(sizeOf($resultMembers->members) == 0) {
		} else {
			foreach($resultMembers->members as $el) {
				$resultJson[] = array(
					'body' => $el
				);
			}
		}
		$this->renderJSON($resultJson);

	}

	public function actionRemoveMemberFromPage()
	{
		$userId = $_GET['userId'];
		$slug = $_GET['slug'];
		$username = $_GET['username'];
		$membersUrl = $this->createUrl("page/$slug/members");
		$result = PageHelper::removeMemberFromPage($userId, $slug);
		if($result->status == 1) {
			Yii::app()->user->setFlash('success', "Removed $username from page");
		} else {
			Yii::app()->user->setFlash('error', "Can't removed $username from page");

		}
		$this->redirect($membersUrl);
		
	}

	public function actionSetRelation()
	{
		$relation = $_GET['relation'];
		$userId = $_GET['userId'];
		$slug = $_GET['slug'];
		$username = $_GET['username'];
		//$membersUrl = $this->createUrl("page/$slug/members");

		$result = PageHelper::setRelation($userId, $slug, $relation);

		$resultJson = array();
		$resultJson[] = array(
				'status'=>$result->status,
				'message'=>$result->message
			);
		$this->renderJSON($resultJson);
	}

}