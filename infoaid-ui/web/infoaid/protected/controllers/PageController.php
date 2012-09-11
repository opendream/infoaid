<?php

class PageController extends IAController
{
	public function actions()
	{
		return array(
			'static'=>array(
				'class'=>'IANoLayoutViewAction',
			),
		);
	}
	
	public function actionIndex($slug)
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

}