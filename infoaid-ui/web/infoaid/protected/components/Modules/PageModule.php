<?php

class PageModule
{
	public function beforeBeforeRender($mainController)
	{
		$mainController->scripts[] = 'colorbox/jquery.colorbox.js';
		$mainController->styles[] = '/js/colorbox/colorbox.css';
	}

	public function afterBeforeRender($mainController)
	{
		$config = json_encode(Yii::app()->params['item']);
		$js = "jQuery.extend(InfoAid.settings, $config)";
		Yii::app()->clientScript
			->registerScript('item-config',$js,CClientScript::POS_HEAD);


		Yii::app()->clientScript
			->registerScript(
				'template',
				"window.Templates = {};",
				CClientScript::POS_HEAD
			);

		Yii::app()->clientScript
			->registerScript(
				'template.post',
				"window.Templates.post = {};",
				CClientScript::POS_HEAD
			);

		$postTypes = array(
			'text' => 'MessagePost',
			'need' => 'Need',
			'resource' => 'Resource',
		);

		foreach ($postTypes as $type => $layoutFile) {
			$template = json_encode(
				$mainController->renderPartial(
					'/post/'. $layoutFile,
					array(),
					true
				)
			);

			$bindingScript = "jQuery.extend(window.Templates.post, {'$layoutFile': ". $template ."});";

			Yii::app()->clientScript
				->registerScript(
					'template.post.'. $type,
					$bindingScript,
					CClientScript::POS_HEAD
				);
		}
		
	}
}