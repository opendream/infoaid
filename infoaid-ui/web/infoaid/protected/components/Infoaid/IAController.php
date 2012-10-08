<?php

class IAController extends CController
{
	public $jquery = TRUE;
	
	public $angular = TRUE;

	public $scripts = array();

	public $styles = array(
		'page.scss',
	);

	public $jsLocale = array(
		'timeago',
	);

	private $modules = array();
	private $_modules = array();

	protected $_app;

	public function renderJSON($content)
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
		$this->initModule();
		$this->runModule('init');

		$this->initLang();
	}

	public function beforeRender($view)
	{
		parent::beforeRender($view);
		$this->runModule('beforeBeforeRender');

		$this->injectLocale();

		if ($this->jquery == TRUE) {
			Yii::app()->clientScript
				->registerScriptFile(Yii::app()->baseUrl .'/js/jquery/jquery.js')
				->registerScriptFile(Yii::app()->baseUrl .'/js/jquery.timeago.js')
				->registerScriptFile(Yii::app()->baseUrl .'/css/bootstrap/js/bootstrap.js')
				->registerScriptFile(Yii::app()->baseUrl .'/js/spin.min.js');
		}

		if ($this->angular == TRUE) {
			Yii::app()->clientScript
				->registerScriptFile(Yii::app()->baseUrl .'/js/angular.js/angular.js')
				->registerScriptFile(Yii::app()->baseUrl .'/js/angular.js/angular-resource.js')
				->registerScriptFile(Yii::app()->baseUrl .'/js/angular.js/angular-bootstrap.js')
				->registerScriptFile(Yii::app()->baseUrl .'/js/select2/select2.js');
		}

		if ($this->jquery && $this->angular) {
			Yii::app()->clientScript
				->registerScriptFile(Yii::app()->baseUrl .'/js/timeago.js');
		}

		$this->injectJSConfig();

		foreach ($this->scripts as $script) {
			Yii::app()->clientScript
				->registerScriptFile(Yii::app()->baseUrl .'/js/'. $script);
		}

		// Publish main css folder.
		$publishedCSSUrl = Yii::app()->getAssetManager()->publish(
			Yii::app()->basePath .'/../css/'
		);

		foreach ($this->styles as $style) {
			if (substr($style, 0, 1) === '/') {
				// Not main css path, then publish as file instead.
				$url = rtrim(Yii::app()->baseUrl, '/');
				$path = rtrim(Yii::app()->basePath, '/');
			}
			else {
				$url = rtrim($publishedCSSUrl, '/') .'/';
				$path = rtrim(Yii::app()->basePath, '/') .'/../css/';
			}

			if (pathinfo($style, PATHINFO_EXTENSION) === 'scss') {
				$publishedURL = Yii::app()->getAssetManager()->publish(
					$path . $style
				);
			}
			else {
				$publishedURL = $url . $style;
			}

			Yii::app()->clientScript
				->registerCssFile($publishedURL);
		}

		$this->runModule('afterBeforeRender');

		return true;
	}

	public function initLang()
	{
		$allowed_language = Yii::app()->params['allowed_language'];

		if (isset($_GET['lang']) && in_array($_GET['lang'], $allowed_language)) {
			Yii::app()->language = $_GET['lang'];
		}		
	}

	public function injectLocale()
	{
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
				->registerScript('locale',"jQuery.locale={};",CClientScript::POS_HEAD);

			$coreMessages = $app->coreMessages;
			foreach ($this->jsLocale as $category) {
				$messages = $coreMessages->getAllMessages($category,$lang);

				// Make as JSON
				$messages = json_encode($messages);

				// Assign values.
				$messages = "jQuery.extend(jQuery.locale, {'$category':". $messages ."});";

				$key = 'locale-'. $category;

				$app->clientScript
					->registerScript($key,$messages,CClientScript::POS_HEAD);
			}
		}
	}

	public function injectJSConfig()
	{
		$config = json_encode(Yii::app()->params['global']);

		$js = "window.InfoAid={settings:{}};jQuery.extend(InfoAid.settings, $config)";

		Yii::app()->clientScript
			->registerScript('infoaid-settings',$js,CClientScript::POS_HEAD);
	}

	public function loadModule($module)
	{
		$this->modules[$module] = $module;
	}

	public function unloadModule($module)
	{
		unset($this->modules[$module]);
	}

	public function initModule()
	{
		foreach ($this->modules as $module) {
			if (! isset($this->_modules[$module])) {
				$this->_modules[$module] = new $module();
			}
		}
	}

	public function runModule($context)
	{
		foreach ($this->modules as $module) {
			$this->_modules[$module]->$context($this);
		}
	}

}
