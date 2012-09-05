<?php

class ApiController extends IAController
{
	protected function renderJSON($content)
	{
		$this->layout = 'application.views.layouts.json';
		$this->renderText($content);
	}

	public function actionFrontPageInfo()
	{
		$this->renderJSON(API::get('front_page/info'));
	}
}