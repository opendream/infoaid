<?php
    $post = PageHelper::getInfoBySlug($slug);    
    $userId = Yii::app()->user->getId();
    $isJoined = PageHelper::isJoined($userId, $slug);
?>

<ul class="page-posts" ng-show="posts.length">
	<li ng-repeat="post in posts" id="post-{{post.id}}">
		<div post ng-controller="PostBodyCtrl"></div>

		<div class="message-comments" ng-controller="CommentCtrl">
			<div id="loadmore-{{post.id}}" class="load-more" ng-click="loadMore('#loadmore-'+post.id)">
				<span class="label label-info" ng-show="post.conversation>3"><i class="icon-comment icon-white"></i>View all {{post.conversation}} comments</span>
				
			</div>

			<ul>
				<li class="thumbnail" ng-repeat="comment in comments">
					<div class="comment-picture">
						<img src="<?php echo Yii::app()->baseUrl; ?>{{comment.picSmall}}"></img>
					</div>

					<div class="comment-details">
						<div class="comment-poster-name">
							<a href="">{{comment.createdBy}}</a>
						</div>
						<div class="comment-body">
							{{comment.message}}
						</div>

						<div class="meta">
							<time class="timeago" datetime="{{comment.lastUpdated}}"
								title="{{comment.lastUpdated}}">
								{{comment.lastUpdated}}
							</time>			
							-				
							<a ng-click="deleteComment(comment)" type="button">delete</a>
						</div>
					</div>
					<div class="clear"></div>					
				</li>				
			</ul>
			<div>
				<form ng-submit="submitComment()">
					<input ng-model='comment' type="text" placeholder="Type a comment..."></input>					
				</form>
			</div>
		</div>
	</li>
</ul>

<div class="clear"></div>

<div class="load-more" ng-show="posts.length">
	<button class="btn" ng-click="loadMore()">
		<i class="icon icon-plus"></i> Load more
	</button>
</div>
<script>
    
</script>
