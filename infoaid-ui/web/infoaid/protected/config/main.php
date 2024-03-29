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
			//'server'=>'http://127.0.0.1:8080/infoaid-api/',
			'server'=>'http://infoaidapi-opendream.rhcloud.com/',
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

			'map' => array(
				'server' => 'http://{s}.tile.cloudmade.com/{key}/997/256/{z}/{x}/{y}.png',
				'api_key' => "195ef107c4e041cfaa584ae805a58030",
				'style_id' => 997,
				'default_zoom' => 13,
				'max_zoom' => 18,
				'default_latlng' => array(
					'lat' => 13.760061108392179,
					'lng' => 100.48919677734375,
				),
				'attribution' => "Map data &copy; Opendream Co., Ltd.",
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
					'name' => 'Food',
					'synonyms' => array(
						'rice', 'ข้าว', 'อาหาร', 'ถุงยังชีพ'
					),
					'icon' => array(
						'id' => '09',
					),
					'unit' => array(
						'base' => 'Kilogram',
						'plural' => 'Kilograms',
						'countable' => false,
						'abbr' => 'kg',
						'variants' => array(
							'kilogram' => array(
								'name' => 'Kilogram',
								'multiplier' => 1,
							),
							'box' => array(
								'name' => 'Box',
								'multiplier' => 0.3,
							),
							'bag' => array(
								'name' => 'Life bag',
								'multiplier' => 2,
							),
						),
					),
				),
				'tent' => array(
					'name' => 'Tent',
					'synonyms' => array(
						'เต้นท์',
					),
					'icon' => array(
						'id' => '01',
					),
					'unit' => array(
						'base' => 'Tent',
						'plural' => 'Tents',
						'countable' => true,
						'abbr' => '',
						'variants' => array(
							'tent' => array(
								'name' => 'Tent',
								'multiplier' => 1,
							),
						),
					),
				),
				'clothes' => array(
					'name' => 'Clothes',
					'synonyms' => array(
						'เสื้อผ้า', 'เสื้อ',
					),
					'icon' => array(
						'id' => '02',
					),
					'unit' => array(
						'base' => 'Set',
						'plural' => 'Sets',
						'countable' => false,
						'abbr' => '',
						'variants' => array(
							'clothes' => array(
								'name' => 'Set',
								'multiplier' => 1,
							),
						),
					),
				),
				'gas' => array(
					'name' => 'Gas',
					'synonyms' => array(
						'ก๊าซ', 'ก๊าซหุงต้ม',
					),
					'icon' => array(
						'id' => '04',
					),
					'unit' => array(
						'base' => 'Litre',
						'plural' => 'Litres',
						'countable' => false,
						'abbr' => 'l',
						'variants' => array(
							'litre' => array(
								'name' => 'Litre',
								'multiplier' => 1,
							),
							'tank' => array(
								'name' => 'Tank',
								'multiplier' => 20,
							),
							'gallon' => array(
								'name' => 'Gallon',
								'multiplier' => 5,
							),
						),
					),
				),
				'fuel' => array(
					'name' => 'Fuel',
					'synonyms' => array(
						'นำ้มัน', 'นำ้มันเชื้อเพลิง',
					),
					'icon' => array(
						'id' => '05',
					),
					'unit' => array(
						'base' => 'Litre',
						'plural' => 'Litres',
						'countable' => false,
						'abbr' => 'l',
						'variants' => array(
							'litre' => array(
								'name' => 'Litre',
								'multiplier' => 1,
							),
							'tank' => array(
								'name' => 'Tank',
								'multiplier' => 20,
							),
							'gallon' => array(
								'name' => 'Gallon',
								'multiplier' => 5,
							),
						),
					),
				),
				'water' => array(
					'name' => 'Water',
					'synonyms' => array(
						'นำ้', 'น้ำดื่ม',
					),
					'icon' => array(
						'id' => '07',
					),
					'unit' => array(
						'base' => 'Litre',
						'plural' => 'Litres',
						'countable' => false,
						'abbr' => 'l',
						'variants' => array(
							'litre' => array(
								'name' => 'Litre',
								'multiplier' => 1,
							),
							'tank' => array(
								'name' => 'Tank',
								'multiplier' => 20,
							),
							'gallon' => array(
								'name' => 'Gallon',
								'multiplier' => 5,
							),
						),
					),
				),
				'boat' => array(
					'name' => 'Boat',
					'synonyms' => array(
						'เรือ',
					),
					'icon' => array(
						'id' => '08',
					),
					'unit' => array(
						'base' => 'Boat',
						'plural' => 'Boats',
						'countable' => true,
						'abbr' => '',
						'variants' => array(
							'boat' => array(
								'name' => 'Boat',
								'multiplier' => 1,
							),
						),
					),
				),
				'man' => array(
					'name' => 'Man',
					'synonyms' => array(
						'คน',
					),
					'icon' => array(
						'id' => '03',
					),
					'unit' => array(
						'base' => 'Man',
						'countable' => true,
						'plural' => 'Men',
						'abbr' => '',
						'variants' => array(
							'man' => array(
								'name' => 'Man',
								'multiplier' => 1,
							),
						),
					),
				),
				'electricity' => array(
					'name' => 'Electricity',
					'synonyms' => array(
						'ไฟฟ้า',
					),
					'icon' => array(
						'id' => '06',
					),
					'unit' => array(
						'base' => 'Watt/hour',
						'plural' => 'Watts/hour',
						'countable' => false,
						'abbr' => 'wph',
						'variants' => array(
							'wph' => array(
								'name' => 'Watt/hour',
								'multiplier' => 1,
							),
							'man' => array(
								'name' => '1 man',
								'multiplier' => 50,
							),
						),
					),
				),
				'medicine' => array(
					'name' => 'Medicine',
					'synonyms' => array(
						'ยา',
					),
					'icon' => array(
						'id' => '10',
					),
					'unit' => array(
						'base' => 'Set',
						'plural' => 'Sets',
						'countable' => false,
						'abbr' => '',
						'variants' => array(
							'set' => array(
								'name' => 'Set',
								'multiplier' => 1,
							),
						),
					),
				),
				'water-closet' => array(
					'name' => 'Water Closet',
					'synonyms' => array(
						'ห้องน้ำ', 'Water Closet'
					),
					'icon' => array(
						'id' => '11',
					),
					'unit' => array(
						'base' => 'Set',
						'plural' => 'Sets',
						'countable' => true,
						'abbr' => '',
						'variants' => array(
							'set' => array(
								'name' => 'Set',
								'multiplier' => 1,
							),
						),
					),
				),
				'pump' => array(
					'name' => 'Water Pump',
					'synonyms' => array(
						'เครื่องสูบน้ำ', 'Water Pump'
					),
					'icon' => array(
						'id' => '12',
					),
					'unit' => array(
						'base' => 'Set',
						'plural' => 'Sets',
						'countable' => true,
						'abbr' => '',
						'variants' => array(
							'set' => array(
								'name' => 'Set',
								'multiplier' => 1,
							),
						),
					),
				),
			),

			// another settings.
		),

		'expertises' => array(
			array('name' => "Accounting", "description" => "Accounting (Accountant, Controller, CPA)"),
			array('name' => "Advertising", "description" => "Advertising (Brand Manager, Creative Director, Advertising Executive)"),
			array('name' => "Brand Strategy", "description" => "Brand Strategy (Brand Strategist, Creative Director, Marketing Director/Manager, Product Manager)"),
			array('name' => "Business Strategy", "description" => "Business Strategy (Senior Business Analyst, Strategic Planning Analyst, Management Consultant, Business Development Director)"),
			array('name' => "Communications", "description" => "Communications (Public Relations Specialist/Manager, Corporate Communications Director)"),
			array('name' => "Copywriting", "description" => "Copywriting (Copywriter, Writer, Communications Manager)"),
			array('name' => "Design", "description" => "Design (Creative Director, Graphic Designer/Artist, Desktop Publisher, Visual Web Designer, UX / UI Designer, Web Designer)"),
			array('name' => "Entrepreneurship", "description" => "Entrepreneurship (Entrepreneur, CEO, Company Founder)"),
			array('name' => "Event Planning", "description" => "Event Planning (Event Planner)"),
			array('name' => "Finance", "description" => "Finance (Financial/Investment Analyst, Management Consultant, Investment Banker, Private Equity/Hedge Fund Analyst)"),
			array('name' => "Fundraising", "description" => "Fundraising (Development Director, Grantwriter, Director/Manager of Business Development, Crowdfunding/Social Media Specialist)"),
			array('name' => "Human Resources", "description" => "Human Resources (Human Resources Manager, Recruiter, Compensation Analyst)"),
			array('name' => "Legal", "description" => "Legal (Attorney, General Counsel, Solicitor, Paralegal)"),
			array('name' => "Marketing", "description" => "Marketing (Marketing Manager/Director, Marketing Analyst)"),
			array('name' => "Multimedia", "description" => "Multimedia (Video Production Manager, Director, Videographer, Multimedia Designer/Specialist, Multimedia Illustrator)"),
			array('name' => "Online Marketing", "description" => "Online Marketing (Digital Marketing Specialist, Social Marketing Manager, Online Marketing Analyst, SEO/SEM Specialist)"),
			array('name' => "Photography", "description" => "Photography (Photographer)"),
			array('name' => "Public Relations", "description" => "Public Relations (Public Relations Specialist/Manager, Communications Manager, Event Planner)"),
			array('name' => "Sales & Business Development", "description" => "Sales & Business Development (Business Development Director/Associate, Sales Manager/Executive)"),
			array('name' => "Social Media", "description" => "Social Media (Social Media Specialist, Digital Community Manager, Social Marketing Specialist/Manager, Online Marketing Analyst/Manager)"),
			array('name' => "Strategic Communications", "description" => "Strategic Communications (Public Relations Specialist, Corporate Communications Manager)"),
			array('name' => "Strategic Marketing", "description" => "Strategic Marketing (VP of Marketing, Brand Strategist, Product Manager)"),
			array('name' => "Strategy", "description" => "Strategy (Senior Business Analyst, Stategic Planning Analyst, Management Consultant, Director of Strategy)"),
			array('name' => "Technology", "description" => "Technology (Software Engineer, IT Architect, Systems Administrator/Engineer, Database Analyst, Web Designer, Blogger)"),
			array('name' => "Writing", "description" => "Writing (Creative Writer, Copywriter, Blogger)"),
		),
	),
);