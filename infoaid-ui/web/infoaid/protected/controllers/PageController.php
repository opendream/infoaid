<?php

class PageController extends IAController
{

	public $styles = array(
		'page.css',
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

	public function actionRemoveMember()
	{
		$resultRemoveMember = API::getJSON('page/removeUserFromPage', array(
			'userId'=>$userId
			)
		);
		$this->render('members');
	}

}