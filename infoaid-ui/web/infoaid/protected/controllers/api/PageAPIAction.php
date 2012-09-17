<?php

class PageAPIAction extends CAction
{
	public function recentPost($slug)
	{
		$this->controller->renderJSON(PageHelper::getRecentPost($slug));
	}

	public function runWithParams($params)
	{
		$keys = array_keys($params);

		if ($keys[0] == 'slug' && $keys[1] == 'method') {

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