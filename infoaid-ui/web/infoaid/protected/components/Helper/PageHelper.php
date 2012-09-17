<?php

class PageHelper
{
	private static $base = 'page/';

	public static function get($slug, $method)
	{
		// Unicode support for slug
		$slug = urlencode($slug);
		
		return API::get(self::$base . $slug .'/'. $method);
	}

	public static function getJSON($slug, $method)
	{
		// Unicode support for slug
		$slug = urlencode($slug);
		return API::getJSON(self::$base . $slug .'/'. $method);
	}

	public static function getInfoBySlug($slug)
	{
		$result = self::getJSON($slug, 'info');
		if (! $result->status) {
			return NULL;
		}
		else {
			return $result;
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

}