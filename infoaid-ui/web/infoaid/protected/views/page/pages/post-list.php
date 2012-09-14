<ul class="page-posts span6">
	<li ng-repeat="post in posts" id="post-{{post.id}}">
		<div class="message-content">
			<div class="message-picture">
				<img src="{{post.picSmall}}"></img>
			</div>

			<div class="message-details">
				<div class="message-poster-name">
					<a href="">{{post.user}}</a>
				</div>
				<div class="message-body">
					{{post.message}}
				</div>

				<div class="meta">
					<time class="timeago" datetime="{{post.dateCreated}}">
						{{post.dateCreated}}
					</time>
				</div>
			</div>

			<div class="clear"></div>
		</div>

		<div class="message-comments" ng-controller="CommentCtrl">
			<div class="load-more" ng-click="loadMore()">
				<span class="label label-info">View all comments</span>
			</div>

			<ul>
				<li class="thumbnail" ng-repeat="comment in comments">
					<div class="comment-picture">
						<img src="{{comment.picSmall}}"></img>
					</div>

					<div class="comment-details">
						<div class="comment-poster-name">
							<a href="">{{comment.user}}</a>
						</div>
						<div class="comment-body">
							{{comment.message}}
						</div>

						<div class="meta">
							<time class="timeago" datetime="{{comment.lastUpdated}}"
								title="{{comment.lastUpdated}}">
								{{comment.lastUpdated}}
							</time>
						</div>
					</div>

					<div class="clear"></div>
				</li>
			</ul>
		</div>
	</li>
</ul>

<div class="clear"></div>

<div class="load-more">
	<button class="btn" ng-click="loadMore()">
		<i class="icon icon-plus"></i> Load more
	</button>
</div>