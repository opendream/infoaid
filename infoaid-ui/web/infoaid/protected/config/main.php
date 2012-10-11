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
		'application.components.Modules.*',
		'application.extensions.*',
		'application.extensions.phamlp.PBMAssetManager',
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
				'user/profile/<userId:\d+>'=>'user/profile',

				'api/post/<id:\d+>/<method:\w+>'=>'api/post',
				'api/page/<slug:[^\/]*>/<method:\w+>'=>'api/page',
				'api/members/<slug:[^\/]*>'=>'api/members',
				'page/doUploadImagePost'=>'page/doUploadImagePost',
				'page/create'=>'page/create',
				'page/joinPage'=>'page/joinPage',
				'page/leavePage'=>'page/leavePage',
				'page/loadMoreMembers'=>'page/loadMoreMembers',
				'page/setRelation'=>'page/setRelation',
				'page/removeMemberFromPage'=>'page/removeMemberFromPage',
				'page/search'=>'page/search',
				'page/<slug:[^\/]*>/<method:\w+>'=>'page/<method>',
				'page/<slug:[^\/]*>'=>'page/view',
				'/api/comment'=>'/api/comment',
				'/api/deleteComment/<commentId:\d+>'=>'/api/deleteComment',
				'/api/postMessage'=>'/api/postMessage',
				'/api/postNeed/'=>'/api/postNeed/',
				'/api/postResource/'=>'/api/postResource',
				'/api/deletePost/<postId:\d+>'=>'/api/deletePost',
				'/api/createItem/'=>'/api/createItem',
				'/api/getPages/'=>'/api/getPages',
				//'/api/items'=>'/api/items',

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
				
				// array(
				// 	'class'=>'CWebLogRoute',
				// 	'ignoreAjaxInFireBug'=>true,
				// 	'showInFireBug'=>true,
				// ),
				
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
			'debug' => false,
			'cache' => false,
		),

		'assetManager' => array(
			//'class' => 'ext.phamlp.PBMAssetManager',
			'class' => 'application.components.Infoaid.IAAssetManager',
			'force' => true,
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
			'class'=>'system.caching.CApcCache',
			//'useMemcached'=>true,
			//'servers'=>array(
			//	array('host'=>'localhost','port'=>11211),
			//),
		),

		'phpThumb'=>array(
			'class'=>'ext.EPhpThumb.EPhpThumb',
			'options'=>array(),
		),

		'request' => array(
			'enableCsrfValidation' => false,
			'csrfTokenName' => 'csrf_token',
		),

	),

	// application-level parameters that can be accessed
	// using Yii::app()->params['paramName']
	'params'=>array(
		// this is used in contact page
		'adminEmail'=>'webmaster@example.com',
		
		'api'=>array(
			'server'=>'http://192.168.1.129:8080/infoaid-api/',
			//'port'=>8080,
			'log_request'=>true,
			'debug_rest'=>false,
		),

		'allowed_language' => array('en', 'th'),

		'global' => array(
			'pageSidebarTopMemberLimit' => 6,

			'browser_warning'=>array(
				'reject' => array(
					'all' => false,
					'msie5' => true,
					'msie6' => true,
					'msie7' => true,
					'msie8' => true,
				),
				'imagePath' => '/js/jReject/images/',
			),
		),

		'profile' => array(
			'photo' => array(
				'prefixUrl' => '/media/profiles/',
				'destinationDir' => dirname(__FILE__).'/../../media/profiles/',
				'defaultAvatar' => array(
					'original' => '/media/profiles/profile_default_original.png',
					'large' => '/media/profiles/profile_default_large.png',
					'small' => '/media/profiles/profile_default_small.png',
				),

				'size' => array(
					'original' => array(
						'width' => '400',
						'height' => '400',
						'suffix' => '_o',
					),

					'large' => array(
						'width' => '160',
						'height' => '160',
						'suffix' => '_l',
					),

					'small' => array(
						'width' => '50',
						'height' => '50',
						'suffix' => '_s',
					),

					'ss' => array(
						'width' => '25',
						'height' => '25',
						'suffix' => '_ss',
					),
				),
			),
		),

		'page' => array(
			'logo' => array(
				'prefixUrl' => '/media/page/logo/',
				'destinationDir' => dirname(__FILE__).'/../../media/page/logo/',

				'size' => array(
					'original' => array(
						'width' => '400',
						'height' => '400',
						'suffix' => '_o',
					),

					'large' => array(
						'width' => '160',
						'height' => '160',
						'suffix' => '_l',
					),

					'small' => array(
						'width' => '50',
						'height' => '50',
						'suffix' => '_s',
					),
				),
			),
		),

		'post' => array(
			'photo' => array(
				'prefixUrl' => '/media/posts/',
				'destinationDir' => dirname(__FILE__).'/../../media/posts/',
				
				'size' => array(
					'original' => array(
						'width' => '4048',
						'height' => '4048',
						'suffix' => '_o',
						'no_adaptive_resize' => true,
					),

					'large' => array(
						'width' => '400',
						'height' => '400',
						'suffix' => '_l',
					),

					'small' => array(
						'width' => '50',
						'height' => '50',
						'suffix' => '_s',
					),
				),
			),
		),

		'item' => array(
			'icon' => array(
				'food' => array(
					'synonyms' => array(
						'rice', 'ข้าว', 'อาหาร', 'ถุงยังชีพ'
					),
					'icon' => array(
						'normal' => 'img/icon-09.png',
						'small' => 'img/icon28-09.png',
					),
				),
				'tent' => array(
					'synonyms' => array(
						'เต้นท์',
					),
					'icon' => array(
						'normal' => 'img/icon-01.png',
						'small' => 'img/icon28-01.png',
					),
				),
				'clothes' => array(
					'synonyms' => array(
						'เสื้อผ้า', 'เสื้อ',
					),
					'icon' => array(
						'normal' => 'img/icon-02.png',
						'small' => 'img/icon28-02.png',
					),
				),
				'gas' => array(
					'synonyms' => array(
						'ก๊าซ', 'ก๊าซหุงต้ม',
					),
					'icon' => array(
						'normal' => 'img/icon-04.png',
						'small' => 'img/icon28-04.png',
					),
				),
				'fuel' => array(
					'synonyms' => array(
						'นำ้มัน', 'นำ้มันเชื้อเพลิง',
					),
					'icon' => array(
						'normal' => 'img/icon-05.png',
						'small' => 'img/icon28-05.png',
					),
				),
				'water' => array(
					'synonyms' => array(
						'นำ้', 'น้ำดื่ม',
					),
					'icon' => array(
						'normal' => 'img/icon-07.png',
						'small' => 'img/icon28-07.png',
					),
				),
			),

			// another settings.
		),
	),
);