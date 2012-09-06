<?php

class IAController extends CController
{
	public $jquery = TRUE;

	public function init()
	{
		if ($this->jquery == TRUE) {
			Yii::app()->clientScript
				->registerScriptFile(Yii::app()->baseUrl .'/js/jquery/jquery.js');
		}
	}

}
