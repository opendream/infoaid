<?php

class IAPhpMessageSource extends CPhpMessageSource
{
	public function getAllMessages($category,$language)
	{
		return $this->loadMessages($category,$language);
	}
}