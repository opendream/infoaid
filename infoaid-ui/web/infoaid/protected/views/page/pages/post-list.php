<?php
    $post = PageHelper::getInfoBySlug($slug);    
    $userId = Yii::app()->user->getId();
    $isJoined = PageHelper::isJoined($userId, $slug);
?>
<div class="modal hide fade" id="myModal" ng-controller="ModalCtrl">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" ng-click="removeNewItem()">&times;</button>
		<h3>Add New Item</h3>
	</div>
	<div class="modal-body">
		<input ng-model="newItem" ng-maxlength="50" type="text" 
    		class="span3" placeholder="Item Name..."></input>
	</div>
	<div class="modal-footer">
		<a href="#" class="btn btn-mini btn-primary" ng-click="removeNewItem()" data-dismiss="modal" aria-hidden="true">Close</a>
		<a href="#" class="btn btn-mini btn-primary" ng-click="setModel(newItem)" aria-hidden="true">Save</a>
	</div>		   	
</div>

<div ng-controller="PostMessageCtrl">	
	<div class="tabbable" ng-init="userId='<?php echo $userId; ?>'; isjoined='<?php echo $isJoined->isJoined; ?>'">
		<ul class="nav nav-pills">
	       	<li class="active"><a href="#tabInfo" data-toggle="tab" class="btn btn-mini btn-link">info</a></li>
	       	<li><a href="#tabNeed" data-toggle="tab" class="btn btn-mini btn-link">request</a></li>
	    </ul>
	    <div class="tab-content">
	        <div class="tab-pane active" id="tabInfo">

	        	<div class="post-form">
		      	    <form ng-submit="postMessage()" class="message-post-form">
		        		<textarea name="inputMsg" ng-model="message" ng-maxlength="140" type="text" class="expanding span5" placeholder="Type info..."></textarea>
		        		<script>
		        			$('textarea.expanding')
		        				.expandingTextarea()
		        				.focus(function (event) {
		        					$('.upload-form-wrapper').slideDown();
		        				});
		        		</script>
		        		<input name="picSmall" ng-model="picSmall" type="hidden" id="picSmall" />
			      		<input name="picOriginal" ng-model="picOriginal" type="hidden" id="picOriginal" />
		        	</form>

		        	<div class="upload-form-wrapper hide">
			        	<form id="fileupload" class="upload-form" method="POST" enctype="multipart/form-data">
				    		<div id="previewImg"></div>

				    		<label for="fileUpload">
				    			<strong>Select an image or video file on your computer.</strong>
				    		</label>
				      		<input id="fileupload" type="file" name="image">

				      		<div class="clear"></div>
				      	</form>

				      	<div class="post-button">
					      	<button ng-click="postMessage()" class="btn btn-inverse pull-right">
				      			Post
				      		</button>
				      	</div>
				      </div>
			      </div>
	        					
	        </div>
	        <div class="tab-pane" id="tabNeed">
	      	    <form validate ng-submit="postRequest()" class="form-inline">
		      		<select class="span3" id="requestPost" ng-model="request" 
		      		ng-options="i.id as i.name for i in items" required>
		  	  	        <option value=""></option>		        
			  	    </select>      		
		        	<input class="span2" ng-model='qty' type="text" required></input>
		        	<input class="btn btn-mini btn-primary" type="submit" id="submit" value="request" />  
		        	<a href="#myModal" openDialog role="button" class="btn  btn-mini btn-primary" data-toggle="modal">Add Item</a>    		
		        </form>        	  
	      	</div>    
	    </div>  
	</div>
</div>

<ul class="page-posts">
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

<div class="clear"></div>

<div class="load-more">
	<button class="btn" ng-click="loadMore()">
		<i class="icon icon-plus"></i> Load more
	</button>
</div>
<script>
    $(document).ready(function() { 
    	$("#requestPost").select2({
    		placeholder: "Select a Need",
    		allowClear: true
    	});    	 
    });
</script>
<script>
    $(function () {
		$('#fileupload').fileupload({
		    url: '<?php echo $this->createUrl("page/doUploadImagePost"); ?>',
		    dataType: 'json',
		    done: function (e, data) {
		        var imgSmall = data.result.small
		        var imgOriginal = data.result.original
		        $('#previewImg').html("<img src="+baseUrl+imgSmall.url+">");
		        $('#picSmall').val(imgSmall.url)
		        $('#picOriginal').val(imgOriginal.url)
		    }
		});
	});
</script>
