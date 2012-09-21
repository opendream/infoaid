<?php

class UserHelper
{
	private static $base = 'user/';

	public static function get($id, $method)
	{
		return API::get(self::$base . $id .'/'. $method);
	}

	public static function getJSON($id, $method)
	{
		return API::getJSON(self::$base . $id .'/'. $method);
	}

	public static function authenticate($username, $password)
	{
		API::$http_user = $username;
		API::$http_pass = $password;
		API::$http_auth = "Basic";

		$result = API::getJSON(self::$base .'authenticate');

		if ($result && $result->status) {
			return $result;
		}
		else {
			return false;
		}
	}

	public static function updateCachedPassword($user_id, $username, $password)
	{
		API::$http_user = $username;
		API::$http_pass = $password;
		API::$http_auth = "Basic";

		$user_cache_key = 'user-'. $user_id;
		$user_cache_data = Yii::app()->cache->get($user_cache_key);
		if ($user_cache_data) {
			$user_cache_data['password'] = $password;

			Yii::app()->cache->set($user_cache_key, $user_cache_data);
		}
		else {
			return false;
		}
	}
}