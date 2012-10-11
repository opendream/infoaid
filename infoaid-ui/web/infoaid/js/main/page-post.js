$(document).ready(function() { 

	var selectTemplate = function (state) {
		console.log(state);
		return "" +
		"<div class=\"item-select\">" +
			'<i class="item-icon item-icon-28-' + state.class + '"></i>' +
			'<span class="item-name">' + state.text + '</span>' +
		"</div>";
	};

	// $("#requestPost").select2({
	// 	placeholder: "Select a Need",
	// 	allowClear: true,
	// 	formatSelection: selectTemplate,
	// 	formatResult: selectTemplate
	// });
	// $("#resourcePost").select2({
	// 	placeholder: "Select a Resource",
	// 	allowClear: true
	// });

	// $('#requestPost').ddslick({
	// });
});

$(function () {
	var $messageform = $('form.message-post-form'),
		$uploadform = $('#fileupload-form')
	;

	// Text area initialize.
	$('textarea.expanding', $messageform)
		.expandingTextarea()
		.focus(function (event) {
			var $uploadFormWrapper = $('.upload-form-wrapper');
			$uploadFormWrapper.slideDown();
			$('a.close', $messageform)
				.fadeIn()
				.tooltip()
				.click(function (event) {
					$(this).fadeOut();
					$uploadFormWrapper.slideUp();
					$('textarea', $messageform).val('');
					// Hacky, do what $.expandingText.opts.resize do.
					$messageform.find('.expandingText div').text('');
				});
		});

	$('#fileupload').fileupload({
	    url: baseUrl + '/page/doUploadImagePost',
	    dataType: 'json',
	    done: function (e, data) {
	    	// Clear error
	    	$('.alert', $uploadform).hide();
	        var imgSmall = data.result.small;
	        var imgLarge = data.result.large;
	        var imgOriginal = data.result.original;
	        $('#previewImg').html("<img src="+baseUrl+imgSmall.url+">");
	        $('#picSmall').val(imgSmall.url);
	        $('#picLarge').val(imgLarge.url);
	        $('#picOriginal').val(imgOriginal.url);
	    },
	    fail: function (e, data) {
	    	var $span = $('span', $uploadform),
	    		$previewImg = $('#previewImg');
	    	;

	    	$previewImg.html('');

	    	if ($span.length === 0) {
	    		$uploadform.prepend('<div class="alert alert-error hide"><span></span><a class="close" data-dismiss="alert" href="#">&times;</a></div>');
	    	}
	    	$span = $('span', $uploadform);
	    	$span.text(data.jqXHR.responseText);
	    	$('.alert-error', $uploadform).show().alert();
	    }
	});
});