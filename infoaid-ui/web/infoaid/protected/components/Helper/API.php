<?php

class API
{
	private static $_server;

	private static $_port;

	private static $_rest;

	public static $config;

	public static function getAPIConfig()
	{
		if (empty(self::$config))
		{
			self::$config = Yii::app()->params['api'];
		}

		return self::$config;
	}

	public static function getServerURI()
	{
		if (empty(self::$_server))
		{
			$config = self::getAPIConfig();

			$server = $config['server'];
			if (! empty($config['port'])) {
				$server = preg_replace('/(\/)$/', ':'. $config['port'] .'\\1', $server);
			}

			self::$_server = $server;
		}

		return $server;
	}

	/**
	 * @Singleton
	 */
	public static function getRESTObject()
	{
		if (! isset(self::$_rest))
		{
			self::$_rest = new RESTClient;
			self::$_rest->initialize(array(
				'server' => self::getServerURI(),
			));
		}

		return self::$_rest;
	}

	public static function getAllowedMethod()
	{
		return array(
			'get',
			'post',
			'put',
			'delete',
		);
	}

	public static function isMethodAllowed($method)
	{
		return in_array($method, self::getAllowedMethod());
	}

	public static function call($method, $uri, $params = array(),
		$format = 'json')
	{
		$rest = self::getRESTObject();
		if (self::isMethodAllowed($method)) {
			$result = $rest->$method($uri, $params, $format);
		}

		return $result;
	}

	public static function get($uri, $params = array(), $format = 'json')
	{
		return self::call('get', $uri, $params, $format);
	}
}