<?php
	$session = new CHttpSession;
	$session->open();
	$userId = 8185;
	$totalLoad = $_GET['totalLoad'];
    $members = PageHelper::getMembers($slug, $totalLoad);
    $totalLoad = $members->totalMembers;
    $isOwner = PageHelper::isOwner($userId, $slug);

    function echoPic($member) {
    	if($member->picOriginal == null) {
            echo '<img src='.Yii::app()->baseUrl.'/media/profiles/profile_default_small.png class="img-polaroid page-members-pic">';
        } else {
            echo '<img src=' . Yii::app()->baseUrl . $member->picSmall . ' class="img-polaroid page-members-pic">';
        }
    }

    function echoProfile($member) {
    	echo '<div><span class="page-members-text">'.CHtml::link($member->username, array()).'</span>';
    	if($member->relation == 'OWNER') {
    		$relation = 'Owner';
    	} else {
    		$relation = 'Member';
    	}
    	echo '<span class="page-members-text" id="page-member-relation-'.$member->id.'">('.$relation.')</span></div>';
    	echo '<span class="page-members-text">'.$member->firstname.'</span>';
    	echo '<span class="page-members-text">'.$member->lastname.'</span>';
    	
    }

    function echoMenu($member, $slug, $totalLoad) {
    	$username = $member->username;
    	echo "<div>
	    		<ul class='nav menu pull-left page-members-menu'>
				    <li class='dropdown' id='menu-page-member-$member->id'>
				        <a href='#menu-page-member-$member->id' class='dropdown-toggle' data-toggle='dropdown'><i class='icon-wrench'></i></a>
				        <ul class='dropdown-menu'>";
				        if($member->relation == 'MEMBER') {
				        	echo "<li>".CHtml::link('Change to Owner', array('page/setRelation','userId'=>$member->id, 'username'=>$member->username, 'slug'=>$slug, 'relation'=>'Owner', 'totalLoad'=>$totalLoad))."</li>";
				        } else if($member->relation == 'OWNER') {
				        	echo "<li>".CHtml::link('Change to Member', array('page/setRelation','userId'=>$member->id, 'username'=>$member->username, 'slug'=>$slug, 'relation'=>'Member', 'totalLoad'=>$totalLoad))."</li>";
				        }
				        echo "<li>".CHtml::link('Remove from group', array('page/removeMemberFromPage','userId'=>$member->id, 'username'=>$member->username, 'slug'=>$slug, 'totalLoad'=>$totalLoad))."</li>
				        </ul>
				    </li>
				</ul>
			</div>";
    }

    function echoContext($member, $slug, $isOwner, $totalLoad) {
    	echoPic($member);
    	echoProfile($member);
    	if($isOwner == 1) {
    		echoMenu($member, $slug, $totalLoad);
    	}
    	
    }

    function echoTable($member, $slug, $isOwner, $type, $totalLoad) {
    	//echo "<div class='span3'>";
    	if($type == 'open') {
    		echo "<tr class='page-members'>";
			echo "<td class='span3'>";
			echoContext($member, $slug, $isOwner, $totalLoad);
			echo "</td>";
    	} else if($type == 'normal') {
    		echo "<td class='span3'>";
			echoContext($member, $slug, $isOwner, $totalLoad);
			echo "</td>";
    	} else if($type == 'close') {
    		echo "<td class='span3'>";
			echoContext($member, $slug, $isOwner, $totalLoad);
			echo "</td>";
			echo "</tr>";
    	}
    	//xattr_get(filename, name)echo "</div>";
    	
    }

?>
<header class="info">
	<div>
		<?php $this->renderPartial('sidebar'); ?>
	</div>
	<div>
		<?php $this->renderPartial('header', array('slug'=>$slug,'id'=>$page->id)); ?>
	</div>
</header>
<div id="page-members" class='span9' ng-app="member" ng-controller="memberController">
	<header>
		<h1>Members</h1>
	</header>
	<div class='flash-message'>
		<?php
			if( Yii::app()->user->hasFlash('error') ) {
		?>
		<span class="alert alert-error">
			<?php
					echo Yii::app()->user->getFlash('error');
				}
			?>
		</span>
		<?php
			if( Yii::app()->user->hasFlash('success') ) {
		?>
		<span class="alert alert-success">
			<?php
					echo Yii::app()->user->getFlash('success');
				}
			?>
		</span>
	</div>
	<table class='page-members'>
		<tbody id='page-members-body'>
			<?php
				$column = 0;
				foreach ($members->members as $member) {
					if($column == 0) {
						echoTable($member, $slug, $isOwner->isOwner, 'open', $totalLoad);
				        $column++;
					} else if($column == 2) {
						echoTable($member, $slug, $isOwner->isOwner, 'close', $totalLoad);
						$column = 0;
					} else {
						echoTable($member, $slug, $isOwner->isOwner, 'normal', $totalLoad);
			            $column++;
					}
				}
			?>
		</tbody>
	</table>
	<div id="loading" class="ajax-loading"></div>
	<div class="load-more" id="load-more">
		<button class="btn" ng-click="loadMore()">
			<i class="icon icon-plus"></i> Load more
		</button>
	</div>
</div>
<script>
	angular.module('memberService', ['ngResource']).
		factory('Member', function ($resource) {
			var Member = $resource('<?php echo $this->createUrl("page/loadMoreMembers"); ?>');

			return Member;
		}).
		factory('Relation', function ($resource) {
			var Relation = $resource('<?php echo $this->createUrl("page/setRelation"); ?>');

			return Relation;
		});

	angular.module('member', ['memberService']);

	

	function memberController($scope, Member, Relation) {
		var opts = {
		  lines: 13, // The number of lines to draw
		  length: 7, // The length of each line
		  width: 4, // The line thickness
		  radius: 10, // The radius of the inner circle
		  corners: 1, // Corner roundness (0..1)
		  rotate: 0, // The rotation offset
		  color: '#000', // #rgb or #rrggbb
		  speed: 1, // Rounds per second
		  trail: 60, // Afterglow percentage
		  shadow: false, // Whether to render a shadow
		  hwaccel: false, // Whether to use hardware acceleration
		  className: 'spinner', // The CSS class to assign to the spinner
		  zIndex: 2e9, // The z-index (defaults to 2000000000)
		  top: 'auto', // Top position relative to parent in px
		  left: 'auto' // Left position relative to parent in px
		};

		$scope.totalLoad = parseInt('<?php echo $totalLoad; ?>');
		/*
		$scope.setRelation = function(userId, username, slug, relation) {
			console.log($scope.setRelation)
			console.log(userId, username, slug, relation)
			Relation.query({
				userId: userId,
				slug: slug,
				relation: relation
			}, function(result) {
					$('#page-member-relation-'+userId).html('('+relation+')')
					var menu = '';
					menu += "<a href='#menu-page-member-"+userId+"'"+"class='dropdown-toggle' data-toggle='dropdown'><i class='icon-wrench'></i></a>";
					menu += "<ul class='dropdown-menu'>";
					if(relation == 'Member') {
			        	menu += "<li onclick='"+$scope.setRelation()+"'><a href='#'>Change to Owner</a></li>";
			        } else if(relation == 'Owner') {
			        	menu += "<li onclick='"+$scope.setRelation()+"'><a href='#'>Change to Member</a></li>";
			        }
			        menu += "<li><a href='#'>Remove from group</a></li></ul>";
					$('#menu-page-member-'+userId).html(menu)
				}
			);
		}
		*/
		$scope.echoPic = function(picUrl) {
			var ret = '';
			if(picUrl == null) {
				ret = '<img src="'+baseUrl+'/media/profiles/profile_default_small.png" class="img-polaroid page-members-pic">';
			} else {
				ret = '<img src="'+baseUrl+'/'+picUrl+'" class="img-polaroid page-members-pic">';
			}
			return ret;
		}

		$scope.echoProfile = function(member) {
			var ret = '';
			var relation = '';
	    	ret += '<div><span class="page-members-text"><a href="'+baseUrl+'">'+member.username+'</a></span>';
	    	if(member.relation == 'OWNER') {
	    		relation = 'Owner';
	    	} else {
	    		relation = 'Member';
	    	}
	    	ret += '<span class="page-members-text" id="page-member-relation-'+member.id+'">('+relation+')</span></div>';
	    	ret += '<span class="page-members-text">'+member.firstname+'</span>';
	    	ret += '<span class="page-members-text">'+member.lastname+'</span>';

	    	return ret;
	    	
	    }

	    $scope.echoMenu = function(member, slug) {
	    	var ret = "<div>";
	    	ret += "<ul class='nav menu pull-left page-members-menu'>";
			ret += "<li class='dropdown' id='menu-page-member-"+member.id+"'>";
			ret += "<a href='#menu-page-member-"+member.id+"'"+"class='dropdown-toggle' data-toggle='dropdown'><i class='icon-wrench'></i></a>";
			ret += "<ul class='dropdown-menu'>";
	        if(member.relation == 'MEMBER') {
	        	ret += "<li><a href='"+baseUrl+"/page/setRelation?userId="+member.id+"&username="+member.username+"&slug="+slug+"&relation=Owner"+"&totalLoad="+$scope.totalLoad+"'>Change to Owner</a></li>";
	        } else if(member.relation == 'OWNER') {
	        	ret += "<li><a href='"+baseUrl+"/page/setRelation?userId="+member.id+"&username="+member.username+"&slug="+slug+"&relation=Member"+"&totalLoad="+$scope.totalLoad+"'>Change to Member</a></li>";
	        }
			ret += "<li><a href='"+baseUrl+"/page/removeMemberFromPage?userId="+member.id+"&username="+member.username+"&slug="+slug+"&totalLoad="+$scope.totalLoad+"'>Remove from Group</a></li></ul></li></ul></div>";
			return ret;
	    }
	    
		$scope.loadMore = function() {
			var target = document.getElementById('loading');
			var spinner = new Spinner(opts).spin(target);
			Member.query({
				slug: '<?php echo $slug; ?>',
				offset: $scope.totalLoad
			}, function(members) {
				var column = 0;
				var ret = '';
				$scope.totalLoad += members.length;
				angular.forEach(members, function (member) {
					var show = member.body;
					if(column == 0) {
						ret += '<tr class="page-members"><td class="span3">'+
							$scope.echoPic(show.picSmall)+
							$scope.echoProfile(show)+
							$scope.echoMenu(show,'<?php echo $slug;?>')+
							'</td>';
						column++;
					} else if(column == 2) {
						ret += '<td class="span3">'+
							$scope.echoPic(show.picSmall)+
							$scope.echoProfile(show)+
							$scope.echoMenu(show,'<?php echo $slug;?>')+
							'</td></tr>';
						column = 0;
					} else {
						ret += '<td class="span3">'+
							$scope.echoPic(show.picSmall)+
							$scope.echoProfile(show)+
							$scope.echoMenu(show,'<?php echo $slug;?>')+
							'</td>';
						column++;
					}
				});
				$('#page-members-body').append(ret);
				ret = '';
				spinner.stop();
			});
		};
	}
</script>