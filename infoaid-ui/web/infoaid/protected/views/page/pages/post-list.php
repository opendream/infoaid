<ul class="page-posts">
	<li ng-repeat="post in posts">
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
			</div>
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
				</div>
			</li>
		</ul>
	</li>
</ul>