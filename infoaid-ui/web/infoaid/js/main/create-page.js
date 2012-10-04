jQuery(document).ready(function ($) {
	var initMap = function () {
		var latCenter = 13.760061108392179,
			lngCenter = 100.48919677734375,
			API_KEY   = "195ef107c4e041cfaa584ae805a58030"
		;

		var lat, lng, zoom = 9;

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

		L.tileLayer('http://192.168.1.134/osm_tiles2/{z}/{x}/{y}.png', {
			attribution: "Map data &copy; Opendream Co., Ltd.",
			maxZoom: 18
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