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

}