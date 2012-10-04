<?php

class ItemAPIAction extends CAction
{

	/**
	 * Actions :
	 *    - info
	 *    - list
	 */

	public function runWithParams($params)
	{
		if (isset($params['slug'])) {
			$slug = $params['slug'];
		}

		if (isset($params['id']) && isset($params['method'])) {

			switch($params['method']) {

				case "comments":
					return $this->comments($params['id'], $params);
					break;

			}

		}
	}

	public function run($actionID=NULL)
	{
		// NOTHING TO DO HERE:
	}
}