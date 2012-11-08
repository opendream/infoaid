<?php

class PageHelper
{
	private static $base = 'page/';

	public static function get($slug, $method, $params = array())
	{
		// Unicode support for slug
		$slug = urlencode($slug);
		
		return API::get(self::$base . $slug .'/'. $method, $params);
	}

	public static function getJSON($slug, $method, $params = array())
	{
		// Unicode support for slug
		$slug = urlencode($slug);
		return API::getJSON(self::$base . $slug .'/'. $method, $params);
	}

	public static function getInfoBySlug($slug)
	{
		$result = self::getJSON($slug, 'info');
		if (! $result->status) {
			return NULL;
		}
		else {
			return $result->page;
		}
	}

	public static function getPageItemsBySlug($slug)
	{
		$result = self::getJSON($slug, 'info');
		if (! $result->status) {
			return NULL;
		}
		else {
			return $result->items;
		}
	}

	public static function getRecentPost($slug, $params)
	{
		$iconSet = self::getIconSet();

		$result = self::getJSON($slug, 'recent_post', $params);
		if (empty($result) || (isset($result->status) && ! $result->status)) {
			return array();
		}
		else {
			$posts = isset($result->posts) ? $result->posts : $result;
			foreach ($posts as $key => &$post) {
				$post->item->class = self::getItemClass($post->item->name);
				$post->item->baseUnit = $iconSet[$post->item->class]['unit']['base'];
				$post->item->countable = $iconSet[$post->item->class]['unit']['countable'];
				$post->item->plural = $iconSet[$post->item->class]['unit']['plural'];
			}

			return $posts;
		}
	}

	public static function getPageRecentPostMessages($slug)
	{
		$result = self::getJSON($slug, 'recent_post');
		if (! $result->status) {
			return NULL;
		}
		else {
			return $result;
		}
	}

	public static function getMembers($slug, $max)
	{
		$result = API::getJSON("page/member", array('max'=>$max, 'slug'=>$slug));
		return $result;
	}

	public static function removeMemberFromPage($userId, $slug)
	{
		$result = API::getJSON("page/removeUserFromPage", array('userId'=>$userId, 'slug'=>$slug));
		return $result;
	}

	public static function setRelation($userId, $slug, $relation)
	{
		$result = API::getJSON("page/setRelation", array('userId'=>$userId, 'slug'=>$slug, 'relation'=>$relation));
		return $result;
	}

	public static function isOwner($userId, $slug)
	{
		$result = API::getJSON("page/isOwner", array('userId'=>$userId, 'slug'=>$slug));
		return $result;
	}

	public static function isJoined($userId, $slug)
	{
		$result = API::getJSON("page/isJoined", array('userId'=>$userId, 'slug'=>$slug));
		return $result;
	}

	public static function leavePage($userId, $slug)
	{
		$result = API::getJSON("page/leavePage", array('userId'=>$userId, 'slug'=>$slug));
		return $result;
	}

	public static function joinPage($userId, $slug)
	{
		$result = API::getJSON("page/joinUs", array('userId'=>$userId, 'slug'=>$slug));
		return $result;
	}

	public static function createPage($params)
	{
		$result = API::postJSON("page/create_page", $params);
		return $result;
	}

	public static function updatePage($params)
	{
		$result = API::postJSON("page/". urlencode($params['slug']) . "/update_page", $params);
		return $result;
	}

	/**
	 * Return TRUE if process is success, otherwise error message.
	 */
	public static function processUploadedPageLogo($fieldName)
	{
		$settings = Yii::app()->params['page']['logo'];
		$newFilename = str_replace('.', '-', uniqid('logo'));

		$imageHelper = new ImageHelper($settings);

		return $imageHelper->processUploadedFile($fieldName, $newFilename);
	}

	public static function getItemHistory($slug, $params)
	{
		$result = self::getJSON($slug, 'item_history', $params);
		if (empty($result) || (isset($result->status) && ! $result->status)) {
			return array();
		}
		else {
			$posts = isset($result->posts) ? $result->posts : $result;
			foreach ($posts as $post) {
				$post->item->class = self::getItemClass($post->item->name);
			}

			return $posts;
		}
	}

	public static function getIconSet() {
		static $iconSet;
		if (! isset($iconSet)) {
			$iconSet = Yii::app()->params['item']['icon'];
		}

		return $iconSet;
	}

	public static function getItemClass($itemName) {
		$iconSet = self::getIconSet();

		foreach ($iconSet as $name => $config) {
			if ($name == strtolower(trim($itemName)) || in_array($itemName, $config['synonyms'])) {
				return $name;
			}
		}
	}

	public static function getProcessedItemsBySlug($slug)
	{
		$items = self::getPageItemsBySlug($slug);
		foreach ($items as &$item) {
			$item->class = self::getItemClass($item->name);
			$item->percentSupplyPerDemand = ($item->resource / $item->need) * 100.00;
			$item->percentDemandPerSupply = 100 - $item->percentSupplyPerDemand;
		}
		return $items;
	}

	public static function generateCSSForItemIcon()
	{
		$iconSet = Yii::app()->params['item']['icon'];
		$cssString = "";
		foreach ($iconSet as $name => $config) {
			$sizes = array('', '24', '64');
			foreach ($sizes as $size) {
				$sizeString = $size ? ('-'. $size) : '';
				$cssString .= " .item-icon$sizeString-$name { background-image: url(". Yii::app()->baseUrl . "/img/icon$size-". $config['icon']['id'] .".png); } ";
			}
		}

		return $cssString;
	}

}