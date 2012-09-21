<?php

// uncomment the following to define a path alias
// Yii::setPathOfAlias('local','path/to/local-folder');

// This is the main Web application configuration. Any writable
// CWebApplication properties can be configured here.
return array(
	'basePath'=>dirname(__FILE__).DIRECTORY_SEPARATOR.'..',
	'name'=>'InfoAid',
	'defaultController'=>'front',

	'language'=>'en',

	// preloading 'log' component
	'preload'=>array('log'),

	// autoloading model and component classes
	'import'=>array(
		'application.models.*',
		'application.components.*',
		'application.components.Helper.*',
		'application.components.Infoaid.*',
		'application.extensions.*',
	),

	'modules'=>array(
		// uncomment the following to enable the Gii tool
		/*
		'gii'=>array(
			'class'=>'system.gii.GiiModule',
			'password'=>'Enter Your Password Here',
			// If removed, Gii defaults to localhost only. Edit carefully to taste.
			'ipFilters'=>array('127.0.0.1','::1'),
		),
		*/
	),

	// application components
	'components'=>array(
		'defaultController'=>'front',
		'user'=>array(
			// enable cookie-based authentication
			'allowAutoLogin'=>true,
			'loginUrl'=>'user/login',
			'returnUrl'=>'/',
		),
		// uncomment the following to enable URLs in path-format
		/*
		'urlManager'=>array(
			'urlFormat'=>'path',
			'rules'=>array(
				'<controller:\w+>/<id:\d+>'=>'<controller>/view',
				'<controller:\w+>/<action:\w+>/<id:\d+>'=>'<controller>/<action>',
				'<controller:\w+>/<action:\w+>'=>'<controller>/<action>',
			),
		),
		*/
		'urlManager'=>array(
			'urlFormat'=>'path',
			'rules'=>array(
				''=>'front/index',
				'user/register'=>'user/create',
				'user/doEdit/<section:\w+>'=>'user/doEdit',
				'user/edit/<section:\w+>'=>'user/edit',

				'api/post/<id:\d+>/<method:\w+>'=>'api/post',
				'api/page/<slug:[^\/]*>/<method:\w+>'=>'api/page',
				'api/members/<slug:[^\/]*>'=>'api/members',
				'page/search'=>'page/search',
				'page/<slug:[^\/]*>'=>'page/view',
				'/api/comment'=>'/api/comment',
				'/api/deleteComment/<commentId:\d+>'=>'/api/deleteComment',
				'/api/postMessage'=>'/api/postMessage',
				'/api/deletePost/<postId:\d+>'=>'/api/deletePost',

				'<controller:\w+>/<id:\d+>'=>'<controller>/view',
				'<controller:\w+>/<action:\w+>/<id:\d+>'=>'<controller>/<action>',
				'<controller:\w+>/<action:\w+>'=>'<controller>/<action>',
			),
		),
		'db'=>array(
			'connectionString' => 'sqlite:'.dirname(__FILE__).'/../data/testdrive.db',
		),
		// uncomment the following to use a MySQL database
		/*
		'db'=>array(
			'connectionString' => 'mysql:host=localhost;dbname=testdrive',
			'emulatePrepare' => true,
			'username' => 'root',
			'password' => '',
			'charset' => 'utf8',
		),
		*/
		'errorHandler'=>array(
			// use 'site/error' action to display errors
			'errorAction'=>'site/error',
		),
		'log'=>array(
			'class'=>'CLogRouter',
			'routes'=>array(
				array(
					'class'=>'CFileLogRoute',
					'levels'=>'debug, error, warning',
				),
				// uncomment the following to show log messages on web pages
				
				array(
					'class'=>'CWebLogRoute',
					'ignoreAjaxInFireBug'=>true,
					'showInFireBug'=>true,
				),
				
			),
		),
		'coreMessages'=>array(
			'class'=>'IAPhpMessageSource',
			'language'=>'en_us',
			'basePath'=>dirname(__FILE__).'/../messages',
		),

		'viewRenderer'=>array(
			'class'=>'ext.phamlp.Haml',

			// delete options below in production
			'ugly' => false,
			'style' => 'nested',
			'debug' => true,
			'cache' => false,
		),

		'assetManager' => array(
			'class' => 'ext.phamlp.PBMAssetManager',
			'parsers' => array(
				'scss' => array(
					'class' => 'ext.phamlp.Sass',
					'output' => 'css',
					'options' => array(
						'style' => 'nested',
					),
				)
			),
		),

		'cache'=>array(
			'class'=>'system.caching.CMemCache',
			'useMemcached'=>true,
			'servers'=>array(
				array('host'=>'localhost','port'=>11211),
			),
		),

		'phpThumb'=>array(
			'class'=>'ext.EPhpThumb.EPhpThumb',
			'options'=>array(),
		),

	),

	// application-level parameters that can be accessed
	// using Yii::app()->params['paramName']
	'params'=>array(
		// this is used in contact page
		'adminEmail'=>'webmaster@example.com',

		'api'=>array(
			'server'=>'http://192.168.1.131:8080/infoaid-api/',
			//'port'=>8080,
		),

		'allowed_language' => array('en', 'th'),
	),
);