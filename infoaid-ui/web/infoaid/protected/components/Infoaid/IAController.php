<?php

class IAController extends CController
{
	public $jquery = TRUE;
	
	public $angular = TRUE;

	public $styles = array();

	public $jsLocale = array(
		'timeago',
	);

	protected $_app;

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
		parent::init();
		$this->_app = Yii::app();

		$this->initLang();

		if ($this->jquery == TRUE) {
			Yii::app()->clientScript
				->registerScriptFile(Yii::app()->baseUrl .'/js/jquery/jquery.js')
				->registerScriptFile(Yii::app()->baseUrl .'/js/jquery.timeago.js');
		}

		if ($this->angular == TRUE) {
			Yii::app()->clientScript
				->registerScriptFile(Yii::app()->baseUrl .'/js/angular.js/angular.js')
				->registerScriptFile(Yii::app()->baseUrl .'/js/angular.js/angular-resource.js')
				->registerScriptFile(Yii::app()->baseUrl .'/js/angular.js/angular-bootstrap.js');
		}

		if ($this->jquery && $this->angular) {
			Yii::app()->clientScript
				->registerScriptFile(Yii::app()->baseUrl .'/js/timeago.js');
		}

		foreach ($this->styles as $style) {
			Yii::app()->clientScript
				->registerCssFile(Yii::app()->baseUrl .'/css/'. $style);
		}
	}

	public function initLang()
	{
		$allowed_language = Yii::app()->params['allowed_language'];

		if (isset($_GET['lang']) && in_array($_GET['lang'], $allowed_language)) {
			Yii::app()->language = $_GET['lang'];
		}

		if ($this->jquery) {
			$this->injectLocaleInJS();
		}
	}

	public function injectLocaleInJS()
	{
		$app = $this->_app;
		$lang = $app->getLanguage();

		if (! empty($this->jsLocale)) {

			$app->clientScript
				->registerScript('locale',"jQuery.locale={};",CClientScript::POS_BEGIN);

			$coreMessages = $app->coreMessages;
			foreach ($this->jsLocale as $category) {
				$messages = $coreMessages->getAllMessages($category,$lang);

				// Make as JSON
				$messages = json_encode($messages);

				// Assign values.
				$messages = "jQuery.extend(jQuery.locale, {'$category':". $messages ."});";

				$key = 'locale-'. $category;

				$app->clientScript
					->registerScript($key,$messages,CClientScript::POS_BEGIN);
			}
		}
	}

}
