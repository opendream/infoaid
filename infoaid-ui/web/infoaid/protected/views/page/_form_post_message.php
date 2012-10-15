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
		      	    <form ng-submit="postMessage($event)" class="message-post-form">
		      	    	<a class="close hide" title="Clear form, delete text you just typed">&times;</a>
		        		<textarea name="inputMsg" ng-model="message" ng-maxlength="5000" type="text" class="expanding span5" placeholder="Type info..."></textarea>
		        		<input name="picSmall" ng-model="picSmall" type="hidden" id="picSmall" />
		        		<input name="picLarge" ng-model="picLarge" type="hidden" id="picLarge" />
			      		<input name="picOriginal" ng-model="picOriginal" type="hidden" id="picOriginal" />

			      		<?php csrf_token_form(); ?>
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
					      	<button ng-click="postMessage($event)" class="btn btn-inverse pull-right" data-loading-text="Posting ..." ng-disabled="!message.length">
				      			Post
				      		</button>
				      	</div>
				      </div>
			      </div>
	        					
	        </div>
	        <div class="tab-pane" id="tabNeed">
	      	    <form validate ng-submit="postRequest()" class="form-inline">

	      			<div id="div-show-selected-need">
				  	    <a href="#select-need-modal" class="span2 btn" role="button" data-toggle="modal">
				  	    	<span ng-show="!requestItem.id">
				  	    		<i class="icon icon-tags"></i> Select a Need ...
				  	    	</span>

				  	    	<span ng-show="requestItem.id">
						  		<i class="item-icon item-icon-19 item-icon-24-{{requestItem.class}}"></i>
		  	    				{{requestItem.name}}
						  	</span>
				  	   	</a>
				  	</div>

			  	    <div id="select-need-modal" class="modal hide fade">
			  	    	<div class="modal-header">
			  	    		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			  	    		<h3>Which resource do you want to request?</h3>
			  	    	</div>
			  	    	<div class="modal-body">
			  	    		<ul class="unstyled">
			  	    			<li ng-repeat="item in items">
			  	    				<label class="need-item-link">
				  	    				<i class="item-icon item-icon-64 item-icon-64-{{item.class}}"></i>
				  	    				{{item.name}}
				  	    				<input class="hide" type="radio" name="request" value="{{item.id}}" ng-model="requestItem" ng-change="selectNeed(item, '#select-need-modal')" />
				  	    			</label>
			  	    			</li>
			  	    		</ul>
			  	    	</div>
			  	    </div>

		        	<input class="span2 need-quantity" ng-model='qty' type="text" required placeholder="Quantity.."></input>

		        	<select class="input-small select-unit" ng-show="requestItem.id" ng-model="units.selectedNeedUnit" ng-options="u.name for u in units.need"></select>

		        	<div class="form-actions">
		        		<button class="btn btn-danger pull-right" type="submit" id="submit">Request</button>
		        	</div>
		        	<a href="#myModal" openDialog role="button" class="btn  btn-mini btn-primary" data-toggle="modal" ng-hide="true">Add Item</a>    		
		        </form>        	  
	      	</div>
	      	<div class="tab-pane" id="tabResource">
	      	    <form validate ng-submit="postResource()" class="form-inline">

			  	    <div id="div-show-selected-resource">
				  	    <a href="#select-resource-modal" class="span2 btn" role="button" data-toggle="modal">
				  	    	<span ng-show="!resourceItem.id">
				  	    		<i class="icon icon-tags"></i> Select a Resource ...
				  	    	</span>

				  	    	<span ng-show="resourceItem.id">
						  		<i class="item-icon item-icon-19 item-icon-24-{{resourceItem.class}}"></i>
		  	    				{{resourceItem.name}}
						  	</span>
				  	   	</a>
				  	</div>

			  	    <div id="select-resource-modal" class="modal hide fade">
			  	    	<div class="modal-header">
			  	    		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			  	    		<h3>Which resource do you want to give?</h3>
			  	    	</div>
			  	    	<div class="modal-body">
			  	    		<ul class="unstyled">
			  	    			<li ng-repeat="item in items">
			  	    				<label class="resource-item-link">
				  	    				<i class="item-icon item-icon-64 item-icon-64-{{item.class}}"></i>
				  	    				{{item.name}}
				  	    				<input class="hide" type="radio" name="resource" value="{{item.id}}" ng-model="resourceItem" ng-change="selectResource(item, '#select-resource-modal')" />
				  	    			</label>
			  	    			</li>
			  	    		</ul>
			  	    	</div>
			  	    </div>

		        	<input class="span2 resource-quantity" ng-model='resourceQty' type="text" required placeholder="Quantity.."></input>

		        	<select class="input-small select-unit" ng-show="resourceItem.id" ng-model="units.selectedResourceUnit" ng-options="u.name for u in units.resource"></select>

		        	<div class="form-actions">
		        		<input class="btn btn-success pull-right" type="submit" id="submitResource" value="Post" />  
		        	</div>

		        	<a href="#myModal" openDialog role="button" class="btn  btn-mini btn-primary" data-toggle="modal" ng-hide="true">Add Item</a>
		        </form>        	  
	      	</div>
	    </div>  
	</div>
</div>