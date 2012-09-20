<div  ng-controller="PostMessageCtrl">
	<div ng-init="memberId=8185"></div>
<div class="tabbable">
  <ul class="nav nav-pills">
    <li class="active"><a href="#tab1" data-toggle="tab" class="btn btn-mini btn-link">info</a></li>
    <li><a href="#tab2" data-toggle="tab" class="btn btn-mini btn-link">need</a></li>
  </ul>
  <div class="tab-content">
    <div class="tab-pane active" id="tab1">
    	<form name="postMsg" ng-submit="postMessage()">
      		<input name="inputMsg" ng-model="message" ng-maxlength="140" type="text" 
      		class="span5" placeholder="Type info..."></input>	      		
      		<span class="error" ng-show="postMsg.inputMsg.$error.maxlength">
      			Message must have 1 to 140 character 
      		</span>
      	</form>
      					
    </div>
    <div class="tab-pane" id="tab2">
      <input class="span5" ng-model='need' type="text" placeholder="..."></input>
    </div>
  </div>  
</div>
<div class="divider"></div>

<ul class="page-posts">
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
					<button class="btn btn-mini btn-link" ng-click="deletePost(post)" type="button">delete</button>
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
							<button class="btn btn-mini btn-link" ng-click="deleteComment(comment)" type="button">delete</button>
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
</div>
<div class="clear"></div>

<div class="load-more">
	<button class="btn" ng-click="loadMore()">
		<i class="icon icon-plus"></i> Load more
	</button>
</div>