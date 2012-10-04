<?php

class PageAPIAction extends CAction
{
	public function recentPost($slug, $params)
	{
		$this->controller->renderJSON(PageHelper::getRecentPost($slug, $params));
	}

	public function itemHistory($slug, $params)
	{
		$this->controller->renderJSON(PageHelper::getItemHistory($slug, $params));
	}

	public function runWithParams($params)
	{
		if (isset($params['slug']) && isset($params['method'])) {

			switch($params['method']) {

				case "recent_post":
					return $this->recentPost($params['slug'], $params);
					break;

				case "item_history":
					return $this->itemHistory($params['slug'], $params);
					break;

			}

		}
	}

	public function run($actionID=NULL)
	{
		// NOTHING TO DO HERE:
	}
}