<?php

class JRejectModule
{
	public function beforeBeforeRender($mainController)
	{
		$mainController->styles[] = '/js/jReject/css/jquery.reject.css';
		$mainController->scripts[] = 'jReject/js/jquery.reject.js';
		$mainController->scripts[] = 'jreject.js';
	}
}