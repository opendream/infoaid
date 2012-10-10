<ul class="page-posts" ng-show="posts.length">
	<li ng-repeat="post in posts" id="post-{{post.id}}">
		<div post ng-controller="PostBodyCtrl"></div>
		<div class="message-comments" ng-controller="CommentCtrl" ng-init="isjoined='<?php echo $isJoined->isJoined; ?>';">
			<div id="loadmore-{{post.id}}" class="load-more" ng-click="loadMore('#loadmore-'+post.id)">
				<span class="label label-info" ng-show="post.conversation>3">
					<i class="icon-comment icon-white"></i>View all {{post.conversation}} comments</span>
			</div>

			<ul>
				<li class="thumbnail" ng-repeat="comment in comments">
					<div class="comment-picture">
						<a href="<?php echo Yii::app()->baseUrl."/user/profile/{{comment.userId}}";?>"><img src="<?php echo Yii::app()->baseUrl; ?>{{comment.picSmall}}"></img></a>
					</div>

					<div class="comment-details">
						<div class="comment-poster-name">
							<a href="<?php echo Yii::app()->baseUrl."/user/profile/{{comment.userId}}";?>">{{comment.createdBy}}</a>
						</div>
						<div class="comment-body">
							{{comment.message}}
						</div>

						<div class="meta">
							<time class="timeago" datetime="{{comment.lastUpdated}}"
								title="{{comment.lastUpdated}}">
								{{comment.lastUpdated}}
							</time>
							<span ng-show="comment.canDelete">
								-				
								<a ng-click="deleteComment(comment)" type="button">delete</a>
							</span>
						</div>
					</div>
					<div class="clear"></div>					
				</li>				
			</ul>
			<div>
				<form ng-submit="submitComment()" ng-show="isjoined">
					
					<input ng-model='comment' type="text" placeholder="Type a comment..."></input>					
				</form>
			</div>
		</div>
	</li>
</ul>

<div class="clear"></div>

<div class="load-more" ng-show="posts.length">
	<button class="btn" ng-click="loadMore($event)">
		<i class="icon icon-arrow-down"></i> Load more
	</button>
</div>
<script>
    
</script>
