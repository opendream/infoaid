<ul class="page-posts">
	<li ng-repeat="post in posts" id="post-{{post.id}}">
		<div class="message-content">
			<div class="message-picture">
				<img src="{{post.picSmall}}"></img>
			</div>

			<div class="message-details">
				<div class="message-poster-name">
					{{post.user}}
				</div>
				<div class="message-body">
					{{post.message}}
				</div>

				<div class="meta">
					<time class="timeago" datetime="{{post.dateCreated}}"
						title="{{post.dateCreated}}">
						{{post.dateCreated}}
					</time>
				</div>
			</div>

			<div class="clear"></div>
		</div>

		<ul class="message-comments">
			<li ng-repeat="comment in post.comments">
				<div class="comment-picture">
					<img src="{{comment.picSmall}}"></img>
				</div>

				<div class="comment-details">
					<div class="comment-poster-name">
						{{comment.user}}
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
	</li>
</ul>

<div class="load-more">
	<button class="btn" ng-click="loadMore()">
		<i class="icon icon-plus"></i> Load more
	</button>
</div>