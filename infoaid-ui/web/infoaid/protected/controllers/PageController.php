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
		$this->render('members', array('slug'=>$slug));
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
		$totalLoad = $_GET['totalLoad'];
		$membersUrl = $this->createUrl("page/$slug/members")."?totalLoad=$totalLoad";

		$userLogin = Yii::app()->user->getId();
		$isOwner = PageHelper::isOwner($userLogin, $slug);
		if($isOwner->isOwner != 1) {
			Yii::app()->user->setFlash('error', "Your permission is not enough to perform this action");
		} else {
			$result = PageHelper::removeMemberFromPage($userId, $slug);
			if($result->status == 1) {
				Yii::app()->user->setFlash('success', "Removed user : $username from page");
			} else {
				Yii::app()->user->setFlash('error', "Can't removed user : $username from page");

			}
		}
		$this->redirect($membersUrl);
		
	}

	public function actionSetRelation()
	{
		$relation = $_GET['relation'];
		$userId = $_GET['userId'];
		$slug = $_GET['slug'];
		$username = $_GET['username'];
		$totalLoad = $_GET['totalLoad'];
		$membersUrl = $this->createUrl("page/$slug/members")."?totalLoad=$totalLoad";

		$userLogin = Yii::app()->user->getId();
		$isOwner = PageHelper::isOwner($userLogin, $slug);
		if($isOwner->isOwner != 1) {
			Yii::app()->user->setFlash('error', "Your permission is not enough to perform this action");
		} else {
			$result = PageHelper::setRelation($userId, $slug, $relation);
			if($result->status == 1) {
				Yii::app()->user->setFlash('success', "Success change user : $username to $relation");
			} else {
				Yii::app()->user->setFlash('error', "Can not change user : $username to $relation");
			}
		}
		$this->redirect($membersUrl);
	}

	public function actionLeavePage()
	{
		$userId = $_GET['userId'];
		$slug = $_GET['slug'];
		$result = PageHelper::leavePage($userId, $slug);
		$this->renderJSON($result);
	}

	public function actionJoinPage()
	{
		$userId = $_GET['userId'];
		$slug = $_GET['slug'];
		$result = PageHelper::joinPage($userId, $slug);
		$this->renderJSON($result);
	}

	public function actionCreate()
	{
		$this->scripts[] = 'leaflet/leaflet-src.js';
		$this->scripts[] = 'expanding.js';
		$this->scripts[] = 'main/create-page.js';
		$this->styles[] = '/js/leaflet/leaflet.css';

		$page = new stdClass();
		if ($_POST['op'] == 'create') {
			$this->doCreatePage($page);
			$this->redirect("/page/$slug");
		}

		$this->render('form_edit', array(
			'page' => $page,
			'op' => 'create',
		));
	}

	public function actionEdit($slug)
	{
		$this->scripts[] = 'leaflet/leaflet-src.js';
		$this->scripts[] = 'expanding.js';
		$this->scripts[] = 'main/create-page.js';
		$this->styles[] = '/js/leaflet/leaflet.css';

		$page = PageHelper::getInfoBySlug($slug);
		if (! $page) {
			throw new CHttpException(404, 'The specified page cannot be found.');
		}

		if ($_POST['op'] == 'edit') {
			$result = $this->doUpdatePage($page);
		}

		if ($result->status) {
			flash($result->message, 'success');
			$page = $result;
			$this->redirect("/page/$slug");
		}
		else {
			flash($result->message, 'error');
		}

		$this->render('form_edit', array(
			'page' => $page,
			'op' => 'edit',
		));
	}

	private function doCreatePage(&$page)
	{
		$params = $_POST;
		$params['userId'] = user()->getId();

		// Process image via helper
		if (! empty($_FILES['logo'])) {
			$photos = PageHelper::processUploadedPageLogo('logo');

			if (! is_array($photos)) {
				flash($photos, 'error');
			}
			else {
				$params['picOriginal'] = $photos['original']['url'];
				$params['picLarge'] = $photos['large']['url'];
				$params['picSmall'] = $photos['small']['url'];
			}
		}
		
		return $result = PageHelper::createPage($params);
	}

	private function doUpdatePage(&$page)
	{
		$params = $_POST;
		$params['userId'] = user()->getId();

		// Process image via helper
		if (! empty($_FILES['logo'])) {
			$photos = PageHelper::processUploadedPageLogo('logo');

			if (! is_array($photos)) {
				flash($photos, 'error');
			}
			else {
				$params['picOriginal'] = $photos['original']['url'];
				$params['picLarge'] = $photos['large']['url'];
				$params['picSmall'] = $photos['small']['url'];
			}
		}
		else {
			$params['picOriginal'] = $post->picOriginal;
			$params['picLarge'] = $post->picLarge;
			$params['picSmall'] = $post->picSmall;
		}

		$params['slug'] = $page->slug;

		return $result = PageHelper::updatePage($params);
	}

}