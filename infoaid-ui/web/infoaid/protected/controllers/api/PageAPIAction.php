<?php

class PageAPIAction extends CAction
{
	public function recentPost($slug)
	{
		$this->controller->renderJSON(PageHelper::getRecentPost($slug));
	}

	public function runWithParams($params)
	{
		if (isset($params['slug']) && isset($params['method'])) {

			switch($params['method']) {

				case "recent_post":
					return $this->recentPost($params['slug']);
					break;

			}

		}
	}

	public function run($actionID=NULL)
	{
		// NOTHING TO DO HERE:
	}
}