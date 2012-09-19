<?php

class IAUserIdentity extends CUserIdentity
{

	private $_id;
	private $_cache_key;

	public function __construct($username, $password)
	{
		parent::__construct($username, $password);
		$this->_cache_key = 'user-'. md5($this->username);
		$this->setState('cache_key', $this->_cache_key);
	}

	public function authenticate()
	{
		$result = UserHelper::authenticate($this->username, $this->password);

		if ($result->status) {

			$this->errorCode = self::ERROR_NONE;

			$this->_id = $result->id;
			$this->setState('user', $result);

			$data = array(
				'username' => $this->username,
				'password' => $this->password,
			);
			Yii::app()->cache->set($this->_cache_key, $data);

			return true;
		}
		else {

			// Clear data.
			Yii::app()->cache->set($this->_cache_key, array());
			$this->errorCode = self::ERROR_UNKNOWN_IDENTITY;

			return false;
		}
	}

	public function getId()
	{
		return $this->_id;
	}	
}