<?php

class PostAPIAction extends CAction
{
	public function comments($id)
	{
		$this->controller->renderJSON(PostHelper::getComments($id));
	}

	public function runWithParams($params)
	{
		if (isset($params['id']) && isset($params['method'])) {

			switch($params['method']) {

				case "comments":
					return $this->comments($params['id']);
					break;

			}

		}
	}

	public function run($actionID=NULL)
	{
		// NOTHING TO DO HERE:
	}
}