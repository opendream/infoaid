jQuery(function ($) {
	var latCenter = 13.760061108392179,
		lngCenter = 100.48919677734375,
		API_KEY   = "195ef107c4e041cfaa584ae805a58030",
		zoom      = 13,
		url       = baseUrl + '/api/frontPageInfo'
	;

	var map = L.map('map').setView([latCenter, lngCenter], zoom);

	L.tileLayer('http://192.168.1.134/osm_tiles2/{z}/{x}/{y}.png', {
		attribution: "Map data &copy; Opendream Co., Ltd.",
		maxZoom: 18
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
				window.location.href = baseUrl + '/index.php/page/' + item.slug;
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