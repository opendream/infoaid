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

		if ($result->status) {
			return $result;
		}
		else {
			return false;
		}
	}
}