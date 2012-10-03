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
	<div class="tabbable" ng-init="userId='<?php echo $userId; ?>'; isjoined='<?php echo $isJoined->isJoined; ?>';" ng-show="isjoined">
		<ul class="nav nav-pills">
	       	<li class="active"><a href="#tabInfo" data-toggle="tab" class="btn btn-mini btn-link">info</a></li>
	       	<li><a href="#tabNeed" data-toggle="tab" class="btn btn-mini btn-link">request</a></li>
	       	<li><a href="#tabResource" data-toggle="tab" class="btn btn-mini btn-link">I have</a></li>
	    </ul>
	    <div class="tab-content">
	        <div class="tab-pane active" id="tabInfo">

	        	<div class="post-form">
		      	    <form ng-submit="postMessage()" class="message-post-form">
		      	    	<a class="close hide" title="Clear form, delete text you just typed">&times;</a>
		        		<textarea name="inputMsg" ng-model="message" ng-maxlength="140" type="text" class="expanding span5" placeholder="Type info..."></textarea>
		        		<input name="picSmall" ng-model="picSmall" type="hidden" id="picSmall" />
			      		<input name="picOriginal" ng-model="picOriginal" type="hidden" id="picOriginal" />
		        	</form>

		        	<div class="upload-form-wrapper hide">
			        	<form id="fileupload-form" class="upload-form" method="POST" enctype="multipart/form-data">
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
		        	<input class="span2" ng-model='qty' type="text" required placeholder="Quantity.."></input>
		        	<input class="btn btn-mini btn-primary" type="submit" id="submit" value="request" />  
		        	<a href="#myModal" openDialog role="button" class="btn  btn-mini btn-primary" data-toggle="modal">Add Item</a>    		
		        </form>        	  
	      	</div>
	      	<div class="tab-pane" id="tabResource">
	      	    <form validate ng-submit="postResource()" class="form-inline">
		      		<select class="span3" id="resourcePost" ng-model="resource" 
		      		ng-options="i.id as i.name for i in items" required>
		  	  	        <option value=""></option>		        
			  	    </select>      		
		        	<input class="span2" ng-model='resourceQty' type="text" required placeholder="Quantity.."></input>
		        	<input class="btn btn-mini btn-primary" type="submit" id="submitResource" value="Post" />  
		        	<a href="#myModal" openDialog role="button" class="btn  btn-mini btn-primary" data-toggle="modal">Add Item</a>
		        </form>        	  
	      	</div>
	    </div>  
	</div>
</div>