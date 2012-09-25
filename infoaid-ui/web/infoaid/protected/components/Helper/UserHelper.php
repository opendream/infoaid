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

	public static function basicInfo($id)
	{
		return API::getJSON('user/showBasicInfo', array('userId' => $id));
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

	public static function updateCachedPassword($userId, $username, $password)
	{
		if ($user_cache_data = self::getUserCachedData($userId)) {
			$user_cache_data['password'] = $password;

			Yii::app()->cache->set($user_cache_key, $user_cache_data);

			return true;
		}
		else {
			return false;
		}
	}

	public static function updateBasicInfo($userId, $data)
	{
		$result = API::getJSON(self::$base .'updateBasicInfo', $data);
		if ($result->status) {
			return $result;
		}
		else {
			return false;
		}
	}

	public static function getUserCachedData($userId)
	{
		$user_cache_key = 'user-'. $userId;
		$user_cache_data = Yii::app()->cache->get($user_cache_key);

		return $user_cache_data;
	}

	/**
	 * Return TRUE if process is success, otherwise error message.
	 */
	public static function processUploadedProfilePhoto($userId)
	{
		$settings = Yii::app()->params['profile']['photo'];
		$newFilename = str_replace('.', '-', uniqid('p', $userId));

		$imageHelper = new ImageHelper($settings);
		$imageHelper->specific = $userId;

		return $imageHelper->processUploadedFile('image', $newFilename);
	}
}