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
	 * Return profile image dir.
	 */
	public static function getProfilePhotoDir($userId)
	{
		$fileDir = Yii::app()->params['profile']['photoDir'] .'/'. $userId .'/';

		// Remove double slashes.
		$fileDir = str_replace('//', '/', $fileDir);
		
		return $fileDir;
	}

	/**
	 * Return TRUE if process is success, otherwise error message.
	 */
	public static function processUploadedProfilePhoto($userId, $name)
	{
		$uploaded = &$_FILES['image'];

		if (empty($uploaded['error'])) {
			// Generate new unique filename
			$newFilename = str_replace('.', '-', uniqid('p', $userId));

			$fileDir = self::getProfilePhotoDir($userId);
			// Make new directory if not exists.
			if (! is_dir($fileDir)) {
				if (! mkdir($fileDir)) {
					return "Can not create directory for image file.";
				}
			}
			// Merge with default profile photo path
			$newFilepath = $fileDir . $newFilename;

			if (move_uploaded_file($uploaded['tmp_name'], $newFilepath)) {
				$return = self::processProfilePhoto($userId, $newFilepath);
				if (! $return) {
					return "Upload success but process fail.";
				}
				else {
					return $return;
				}
			}
			else {
				return "Upload success but can not keep file in the system.";
			}

		}
		else {
			return $uploaded['error'];
		}
	}

	public static function processProfilePhoto($userId, $filepath)
	{
		$fileDir = self::getProfilePhotoDir($userId);
		$photos = self::convertProfilePhoto($filepath);

		// Convert path to URL
		$_photos = array();
		$prefix = Yii::app()->params['profile']['photoUrlPrefix'];
		foreach ($photos as $size =>$path) {
			$_photos[$size] = array(
				'path' => $path,
				'url' => $prefix . $userId .'/'. basename($path),
			);
		}

		return $_photos;
	}

	public static function convertProfilePhoto($filepath)
	{
		$dir = dirname($filepath);
		$pathinfo = pathinfo($filepath);
		$filename = $pathinfo['filename'];
		$extension = $pathinfo['extension'];

		$processed = array();
		$profiles = Yii::app()->params['profile']['size'];
		foreach ($profiles as $name => $config) {

			// Load config
			$width = $config['width'];
			$height = $config['height'];
			$suffix = $config['suffix'];

			$destname = $filename . $suffix .'.png';
			$destpath = $dir .'/'. $destname;

			try {
				$thumb = Yii::app()->phpThumb->create($filepath);

				$thumb->adaptiveResize($width, $height);
				$thumb->save($destpath, 'png');

				$processed[$name] = $destpath;
			}
			catch (Exception $e) {
				Yii::log($e, 'error', 'profile photo');
				return false;
			}

		}

		return $processed;
	}
}