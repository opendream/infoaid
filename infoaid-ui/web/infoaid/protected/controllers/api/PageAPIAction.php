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

	public function actionInfo($params)
	{
		$this->controller->renderJSON(PageHelper::getInfoBySlug($params['slug']));
	}

	public function actionItems($params)
	{
		$items = PageHelper::getPageItemsBySlug($params['slug']);
		foreach ($items as &$item) {
			$item->percentSupplyPerDemand = ($item->resource / $item->need) * 100.00;
			$item->percentDemandPerSupply = 100 - $item->percentSupplyPerDemand;
		}
		$this->controller->renderJSON($items);
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

				default:
					return $this->{'action' . ucfirst($params['method'])}($params);
					break;

			}

		}
	}

	public function run($actionID=NULL)
	{
		// NOTHING TO DO HERE:
	}
}