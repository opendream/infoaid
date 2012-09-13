jQuery(document).ready(function ($) {
	$('.timeago').timeago();
});

jQuery(function ($) {
	$.timeago.settings.strings = $.locale.timeago;
});

angular.module('time', []).
	directive('timeago', function ($timeout) {
		return {
			restrict: 'C',
			link: function (scope, element, attrs) {

				scope.$watch(attrs.datetime, function (value) {
					element.text(attrs.datetime);

					if (!element.data("timeago")) {
						jQuery(element).timeago();
					}
				});

			}
		};
	});