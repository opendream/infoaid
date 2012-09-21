<?php
	$session = new CHttpSession;
	$session->open();
	$userId = 8185;
	$slug = '8185-page';
    $members = PageHelper::getJSON($slug, "members");
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

    function echoMenu($member, $slug) {
    	$username = $member->username;
    	echo "<div>
	    		<ul class='nav menu pull-left page-members-menu'>
				    <li class='dropdown' id='menu-page-member-$member->id'>
				        <a href='#menu-page-member-$member->id' class='dropdown-toggle' data-toggle='dropdown'><i class='icon-wrench'></i></a>
				        <ul class='dropdown-menu'>";
				        if($member->relation == 'MEMBER') {
				        	echo "<li ng-click='setRelation($member->id,\"$username\", \"$slug\", \"Owner\")'><a href='#'>".'Change to Owner'."</a></li>";
				        } else if($member->relation == 'OWNER') {
				        	echo "<li ng-click='setRelation($member->id,\"$username\", \"$slug\", \"Member\")'><a href='#'>".'Change to Member'."</a></li>";
				        }
				        echo "<li><a href='#'>".CHtml::link('Remove from group', array('page/removeMemberFromPage','userId'=>$member->id, 'username'=>$member->username, 'slug'=>$slug))."</a></li>
				        </ul>
				    </li>
				</ul>
			</div>";
    }

    function echoContext($member, $slug, $isOwner) {
    	echoPic($member);
    	echoProfile($member);
    	if($isOwner == 1) {
    		echoMenu($member, $slug);
    	}
    	
    }

    function echoTable($member, $slug, $isOwner, $type) {
    	//echo "<div class='span3'>";
    	if($type == 'open') {
    		echo "<tr class='page-members'>";
			echo "<td class='span3'>";
			echoContext($member, $slug, $isOwner);
			echo "</td>";
    	} else if($type == 'normal') {
    		echo "<td class='span3'>";
			echoContext($member, $slug, $isOwner);
			echo "</td>";
    	} else if($type == 'close') {
    		echo "<td class='span3'>";
			echoContext($member, $slug, $isOwner);
			echo "</td>";
			echo "</tr>";
    	}
    	//xattr_get(filename, name)echo "</div>";
    	
    }

?>
<div id="page-members" class='span9' ng-app="member" ng-controller="memberController">
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
						echoTable($member, $slug, $isOwner->isOwner, 'open');
				        $column++;
					} else if($column == 2) {
						echoTable($member, $slug, $isOwner->isOwner, 'close');
						$column = 0;
					} else {
						echoTable($member, $slug, $isOwner->isOwner, 'normal');
			            $column++;
					}
				}
			?>
		</tbody>
	</table>
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
	        	ret += "<li ng-click='test()''><a href='#'>Change to Owner</a></li>";
	        } else if(member.relation == 'OWNER') {
	        	ret += "<li ng-click='test()'><a href='#'>Change to Member</a></li>";
	        }
			ret += "<li><a href='#'>Remove from group</a></li></ul></li></ul></div>";
			return ret;
	    }

		$scope.loadMore = function() {
			Member.query({
				slug: '<?php echo $slug; ?>',
				offset: 0//'<?php echo sizeOf($members->members); ?>'
			}, function(members) {
				var column = 0;
				var ret = '';
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
			});
		};
	}
</script>