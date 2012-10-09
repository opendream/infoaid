<?php

class FrontController extends IAController
{
	public function actionIndex()
	{
		$this->actionMap();
	}

	public function actionMap()
	{
		$this->scripts[] = 'leaflet/leaflet-src.js';
		$this->scripts[] = 'tipsy/javascripts/jquery.tipsy.js';
		$this->styles[] = '/js/leaflet/leaflet.css';
		$this->styles[] = '/js/tipsy/stylesheets/tipsy.css';
		$this->styles[] = 'map.css';

		$this->scripts[] = 'main/front.js';

		$this->render('index');
	}

}