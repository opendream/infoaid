jQuery(document).ready(function ($) {
	var initMap = function () {
		var config    = InfoAid.settings.map,
			latCenter = config.default_latlng.lat,
			lngCenter = config.default_latlng.lng
		;

		var lat, lng, zoom = config.default_zoom;

		if ($('#lat').val() && $('#lng').val()) {
			lat = parseFloat($('#lat').val());
			lng = parseFloat($('#lng').val());
			zoom = 15;
		}
		else {
			lat = latCenter;
			lng = lngCenter;
		}

		var map = L.map('map').setView([lat, lng], zoom);

		L.tileLayer(config.server, {
			key: config.api_key,
			styleId: config.style_id,
			attribution: config.attribution,
			maxZoom: config.max_zoom
		}).addTo(map);

		var marker = L.marker([lat, lng], {
			draggable: true
		})
		.on('dragend', function (event) {
			var latlng = marker.getLatLng();
			$('#lat').val(latlng.lat);
			$('#lng').val(latlng.lng);
		})
		.addTo(map);
	};

	initMap();
});