<?php

class API
{
	private static $_server;

	private static $_port;

	private static $_rest;

	public static $config;

	public static $http_user;
	public static $http_pass;
	public static $http_auth;

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

	public static function set_header($name, $value)
	{
		$rest = self::getRESTObject();
		$rest->set_header($name, $value);
	}

	public static function isMethodAllowed($method)
	{
		return in_array($method, self::getAllowedMethod());
	}

	public static function call($method, $uri, $params = array(),
		$format = NULL, $options = array())
	{
		$rest = self::getRESTObject();

		// Use Basic Authenticate if supply
		if (self::$http_user && self::$http_pass) {
			$rest->http_user = self::$http_user;
			$rest->http_pass = self::$http_pass;
		}
		else {
			$user_cache_key = Yii::app()->user->getState('user_cache_key');

			if ($user_cache_key) {
				$data = Yii::app()->cache->get($user_cache_key);

				$username = $data['username'];
				$password = $data['password'];

				$rest->http_user = $username;
				$rest->http_pass = $password;
			}
		}
		$rest->http_auth = 'Basic';

		if (self::isMethodAllowed($method)) {
			foreach ($options as $key => $option) {
				$rest->option($key, $option);
			}
			$result = $rest->$method($uri, $params, $format);

			$api_params = param('api');
			if ($api_params['debug_rest']) {
				$rest->debug();
			}

			$dbg_msg  = strtoupper($method) ." $uri";
			$dbg_msg .= "\nAUTHENTICATION = ";
			$dbg_msg .= "\nuser : ". $rest->http_user;
			$dbg_msg .= "\npass : ". $rest->http_pass;
			$dbg_msg .= "\nauth : ". $rest->http_auth;
			$dbg_msg .= "\nPARAMS = \n";
			$dbg_msg .= print_r($params, 1);
			$dbg_msg .= "\nRESULTS = HEADER (". $rest->status() .")\n";
			$dbg_msg .= print_r($result, 1);

			if ($api_params['log_request']) {
				Yii::log($dbg_msg, 'debug', 'API');
			}

			if ($rest->status() != 200) {
				return false;
			}
		}

		return $result;
	}

	public static function get($uri, $params = array(), $format = NULL)
	{
		return self::call('get', $uri, $params, $format);
	}

	public static function getJSON($uri, $params = array())
	{
		return self::get($uri, $params, 'json');
	}

	public static function post($uri, $params = array(), $format = NULL, $options = array())
	{
		return self::call('post', $uri, $params, $format, $options);
	}

	public static function postJSON($uri, $params = array(), $options = array())
	{
		return self::call('post', $uri, $params, 'json', $options);
	}
}