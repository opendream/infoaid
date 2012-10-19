jQuery(function ($) {
	var config    = InfoAid.settings.map,
		latCenter = config.default_latlng.lat,
		lngCenter = config.default_latlng.lng,
		zoom      = config.default_zoom,
		url       = baseUrl + '/api/frontPageInfo'
	;

	var map = L.map('map').setView([latCenter, lngCenter], zoom);

	L.tileLayer(config.server, {
		key: config.api_key,
		styleId: config.style_id,
		attribution: config.attribution,
		maxZoom: config.max_zoom
	}).addTo(map);

	var autoNS = function() {
        return $(this).offset().top > ($(document).scrollTop() + 200) ? 's' : 'n';
    };

	var addMarker = function (map, lat, lng, item) {
		var marker = L.marker([lat, lng]).addTo(map);

		var imageDiv = marker._icon;
		$(imageDiv)
			.css({'cursor': 'pointer'})
			.tipsy({
				delayOut: 100,
				fade: true,
				gravity: autoNS,
				html: true,
				title: function () {
					return renderMarkerTooltip(item);
				},
				trigger: 'hover'
			})
			.click(function (evt) {
				window.location.href = baseUrl + '/page/' + item.slug;
			});
	};

	var renderMarkerTooltip = function (item) {
		var header = '<header><h1>' + item.name + '</h1></header>';

		var needs = item.needs;
		if (needs.length === 0) {
			return header;
		}

		needs_html = '<div class="needs-overview"><ul>';
		overview_needs = needs.slice(0, 3);
		$.each(overview_needs, function (index, item) {
			needs_html +=
				'<li>' +
					'<span class="message">' + item.item + '</span> : ' +
					'<span class="quantity">' + item.quantity.toString() + '</span>'
				'</li>'
			;
		});
		needs_html += '</ul></div>';

		var more = '';
		if (needs.length > 3) {
			more = '<div class="more">...</div>';
		}

		return header + needs_html + more;
	};

	$.getJSON(url, function (resp) {
		$.each(resp.pages, function (index, item) {
			if (item.lat && item.lng) {
				addMarker(map, item.lat, item.lng, item);
			}
		});
	});
});