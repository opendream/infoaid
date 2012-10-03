<?php
	$session = new CHttpSession;
	$session->open();
	//$userId = 8185;
	$userId = Yii::app()->user->getId();
	$totalLoad = $_GET['totalLoad'];
    $members = PageHelper::getMembers($slug, $totalLoad);
    $totalLoad = $members->totalMembers;
    $isOwner = PageHelper::isOwner($userId, $slug);
    $page = PageHelper::getInfoBySlug($slug);

    function echoPic($member) {
    	if($member->picOriginal == null) {
            echo '<a href="'.Yii::app()->baseUrl.'/user/profile/'.$member->id.'"><img src='.Yii::app()->baseUrl.'/media/profiles/profile_default_small.png class="img-polaroid page-members-pic"></a>';
        } else {
            echo '<a href="'.Yii::app()->baseUrl.'/user/profile/'.$member->id.'"><img src=' .Yii::app()->baseUrl. $member->picSmall . ' class="img-polaroid page-members-pic"></a>';
        }
    }

    function echoProfile($member) {
    	echo '<div><span class="page-members-text">'.CHtml::link($member->username, array("user/profile/$member->id")).'</span>';
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
				        	echo "<li><a onclick='return confirm(\"Are you sure you want to change this user to Owner?\")' href=".Yii::app()->baseUrl.'/page/setRelation?userId='.$member->id.'&username='.$member->username.'&slug='.$slug.'&relation=Owner&totalLoad='.$totalLoad.">Change To Owner</a></li>";
				        } else if($member->relation == 'OWNER') {
				        	echo "<li><a onclick='return confirm(\"Are you sure you want to change this user to Member?\")' href=".Yii::app()->baseUrl.'/page/setRelation?userId='.$member->id.'&username='.$member->username.'&slug='.$slug.'&relation=Member&totalLoad='.$totalLoad.">Change To Member</a></li>";
				        }
				        echo "<li><a onclick='return confirm(\"Are you sure you want to remove this user from page?\")' href=".Yii::app()->baseUrl.'/page/removeMemberFromPage?userId='.$member->id.'&username='.$member->username.'&slug='.$slug.'&totalLoad='.$totalLoad.">Remove from group</a></li>
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

    function echoTable($member, $slug, $isOwner, $totalLoad) {
    		echo "<div class='span3 page-member-margin'>";
			echoContext($member, $slug, $isOwner, $totalLoad);
			echo "</div>";
    	//xattr_get(filename, name)echo "</div>";
    	
    }

?>
<header class="info" ng-app='member'>
	<div>
		<?php $this->renderPartial('sidebar', array(
							'userId' => $userId						
						)); ?>
	</div>
	<div>
		<?php $this->renderPartial('header', array('slug'=>$slug, 'id'=>$page->id)); ?>
	</div>
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
<div id="page-members" ng-app="member" class="page-members" ng-controller="memberController">
	<header>
		<h1 class="header-members">Members</h1><div id='showing'>(Showing <?php echo $totalLoad;?> members)</div>
	</header>
	<div id="page-members-body" class="page-members">
			<?php
				foreach ($members->members as $member) {
					echoTable($member, $slug, $isOwner->isOwner, $totalLoad);
				}
			?>
	</div>


	
	<div class="load-more" id="load-more">
		<button class="btn" ng-click="loadMore()" id="load-more-button">
			<i class="icon icon-plus"></i> Load more
		</button>
		<div id="loading" class="ajax-loading"></div>
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

	angular.module('member', ['memberService', 'headerService']);

	

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

		$scope.echoPic = function(member) {
			var ret = '';
			if(member.picSmall == null) {
				ret = '<a href="'+baseUrl+'/user/profile/'+member.id+'"><img src="'+baseUrl+'/media/profiles/profile_default_small.png" class="img-polaroid page-members-pic"></a>';
			} else {
				ret = '<a href="'+baseUrl+'/user/profile/'+member.id+'"><img src="'+baseUrl+'/'+member.picSmall+'" class="img-polaroid page-members-pic"></a>';
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
	        	ret += "<li><a onclick='return confirm(\"Are you sure you want to change this user to Owner?\")' href='"+baseUrl+"/page/setRelation?userId="+member.id+"&username="+member.username+"&slug="+slug+"&relation=Owner"+"&totalLoad="+$scope.totalLoad+"'>Change to Owner</a></li>";
	        } else if(member.relation == 'OWNER') {
	        	ret += "<li><a onclick='return confirm(\"Are you sure you want to remove this user to Member?\")' href='"+baseUrl+"/page/setRelation?userId="+member.id+"&username="+member.username+"&slug="+slug+"&relation=Member"+"&totalLoad="+$scope.totalLoad+"'>Change to Member</a></li>";
	        }
			ret += "<li><a onclick='return confirm(\"Are you sure you want to remove this user from page?\")' href='"+baseUrl+"/page/removeMemberFromPage?userId="+member.id+"&username="+member.username+"&slug="+slug+"&totalLoad="+$scope.totalLoad+"'>Remove from Group</a></li></ul></li></ul></div>";
			return ret;
	    }

	    $scope.checkIsOwnerEchoMenu = function(member) {
	    	var isOwner = '<?php echo $isOwner->isOwner; ?>';
	    	var ret = '';
	    	if(isOwner == '1') {
	    		ret = $scope.echoMenu(member,'<?php echo $slug;?>');
	    	}
	    	return ret;
	    }
	    
		$scope.loadMore = function() {
			var target = document.getElementById('loading');
			var spinner = new Spinner(opts).spin(target);
			$('#load-more-button').hide();
			Member.query({
				slug: '<?php echo $slug; ?>',
				offset: $scope.totalLoad
			}, function(members) {
				var totalMembers = members.length
				if(totalMembers == 0) {
					$('#load-more-button').hide();
				} else {
					$('#load-more-button').show();
					var ret = '';
					$scope.totalLoad += members.length;
					angular.forEach(members, function (member) {
						var show = member.body;
							ret += '<div class="span3 page-member-margin">'+
								$scope.echoPic(show)+
								$scope.echoProfile(show)+
								$scope.checkIsOwnerEchoMenu(show)+
								'</div>';

					});
					$('#page-members-body').append(ret);
					ret = '';
					$('#showing').html('(Showing ' + $scope.totalLoad + ' members)');
				}
				spinner.stop();
			});
		};
	}
</script>