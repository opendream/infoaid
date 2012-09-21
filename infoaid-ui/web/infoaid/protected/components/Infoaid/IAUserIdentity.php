<?php

class IAUserIdentity extends CUserIdentity
{

	private $_id;
	private $_cache_key;

	public function __construct($username, $password)
	{
		parent::__construct($username, $password);
	}

	public function authenticate()
	{
		$result = UserHelper::authenticate($this->username, $this->password);

		if ($result->status) {

			$this->errorCode = self::ERROR_NONE;

			$this->_id = $result->id;
			$this->setState('user', $result);

			$user_cache_key = 'user-'. $result->id;
			$this->setState('user_cache_key', $user_cache_key);

			$data = array(
				'username' => $this->username,
				'password' => $this->password,
			);
			Yii::app()->cache->set($user_cache_key, $data);

			return true;
		}
		else {
			$this->errorCode = self::ERROR_UNKNOWN_IDENTITY;

			// Clear data.
			Yii::app()->cache->set($user_cache_key, '');

			return false;
		}
	}

	public function getId()
	{
		return $this->_id;
	}	
}