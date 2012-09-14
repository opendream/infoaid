<div style="width:100%; height:400px" id="map"></div>

<?php Yii::app()->clientScript->registerScriptFile(Yii::app()->baseUrl .'/js/tipsy/javascripts/jquery.tipsy.js'); ?>
<?php Yii::app()->clientScript->registerCssFile(Yii::app()->baseUrl .'/js/tipsy/stylesheets/tipsy.css'); ?>
<?php Yii::app()->clientScript->registerCssFile(Yii::app()->baseUrl .'/css/map.css'); ?>
<?php Yii::app()->clientScript->registerScriptFile(Yii::app()->baseUrl .'/js/openlayers/OpenLayers.debug.js'); ?>
<?php Yii::app()->clientScript->registerScriptFile("http://maps.google.com/maps/api/js?v=3.8&sensor=false"); ?>

<script>
jQuery(function ($) {
	const BASE_URL = "<?php echo Yii::app()->baseUrl; ?>";
	const WGS1984 = new OpenLayers.Projection("EPSG:4326");
	const SPHERICALMERCATOR = new OpenLayers.Projection("EPSG:900913");
	const IMG_CIRCLE_RED = BASE_URL + '/js/openlayers/img/marker-circle-red.png';

	var url = "<?php echo $this->createUrl('api/frontPageInfo'); ?>";
	map = new OpenLayers.Map('map');
	var goo = new OpenLayers.Layer.Google(
		"Google Map",
		{
			sphericalMercator: true,
			maxExtent: new OpenLayers.Bounds(-20037508.34, -20037508.34, 20037508.34, 20037508.34)
		}
	);

	var markers = new OpenLayers.Layer.Markers( "Markers" );
	map.addLayer(markers);

	var addMarker = function (lat, lon, item) {
		var size = new OpenLayers.Size('30%', '30%');
		var offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);
		var icon = new OpenLayers.Icon(IMG_CIRCLE_RED, size, offset);

		var lonlat = new OpenLayers.LonLat(lon, lat);
		lonlat.transform(WGS1984, SPHERICALMERCATOR);

		var marker = new OpenLayers.Marker(lonlat, icon);
		markers.addMarker(marker);

		var imageDiv = marker.icon.imageDiv;
		$(imageDiv)
			.css({'cursor': 'pointer'})
			.tipsy({
				delayOut: 100,
				fade: true,
				gravity: $.fn.tipsy.autoWE,
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
					'<span class="message">' + item.message + '</span> : ' +
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

	map.addLayer(goo);

	var center = new OpenLayers.LonLat(100.491882324219, 13.7516899108887);
	center.transform(WGS1984, SPHERICALMERCATOR);
	map.setCenter(center, 6);

	$.getJSON(url, function (resp) {
		$.each(resp.pages, function (index, item) {
			addMarker(item.lat, item.lng, item);
		});
	});
});
</script>