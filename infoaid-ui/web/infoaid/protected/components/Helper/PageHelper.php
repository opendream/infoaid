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

	public static function getRecentPost($slug)
	{
		$result = self::getJSON($slug, 'recent_post');
		if (empty($result) || (isset($result->status) && ! $result->status)) {
			return array();
		}
		else {
			return isset($result->posts) ? $result->posts : $result;
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

}