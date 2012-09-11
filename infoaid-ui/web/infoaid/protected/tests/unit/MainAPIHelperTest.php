<?php

class MainAPIHelperTest extends CTestCase
{

	public function testGetSummaryInfoList()
	{
		$info_list = MainAPIHelper::getSummaryInfoList();

		print_r($info_list);
	}
}