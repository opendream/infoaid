<?php

class PostHelper
{
	private static $base = 'post/';

	public static function get($id, $method)
	{
		return API::get(self::$base . $id .'/'. $method);
	}

	public static function getJSON($id, $method)
	{
		return API::getJSON(self::$base . $id .'/'. $method);
	}

	public static function getComments($id)
	{
		$result = self::getJSON($id, 'comments');
		if (empty($result) || (isset($result->status) && ! $result->status)) {
			return array();
		}
		else {
			return isset($result->comments) ? $result->comments : $result;
		}
	}
}