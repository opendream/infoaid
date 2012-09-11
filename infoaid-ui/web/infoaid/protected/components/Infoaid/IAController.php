<?php

class IAController extends CController
{
	public $jquery = TRUE;
	
	public $angular = TRUE;

	public $styles = array();

	protected function renderJSON($content)
	{
		$this->layout = 'application.views.layouts.json';
		if (! is_string($content)) {
			$content = json_encode($content);
		}
		$this->renderText($content);
	}

	public function init()
	{
		if ($this->jquery == TRUE) {
			Yii::app()->clientScript
				->registerScriptFile(Yii::app()->baseUrl .'/js/jquery/jquery.js')
				->registerScriptFile(Yii::app()->baseUrl .'/js/jquery.timeago.js')
				->registerScriptFile(Yii::app()->baseUrl .'/js/timeago.js');
		}

		if ($this->angular == TRUE) {
			Yii::app()->clientScript
				->registerScriptFile(Yii::app()->baseUrl .'/js/angular.js/angular.js')
				->registerScriptFile(Yii::app()->baseUrl .'/js/angular.js/angular-resource.js')
				->registerScriptFile(Yii::app()->baseUrl .'/js/angular.js/angular-bootstrap.js');
		}

		foreach ($this->styles as $style) {
			Yii::app()->clientScript
				->registerCssFile(Yii::app()->baseUrl .'/css/'. $style);
		}
	}

}
