<?php
    $slugEncode = urlencode($slug);
    $post = API::getJSON("page/$slugEncode/info");
?>
<div id="page-header-<?php echo $id; ?>" class='page-header'>
    <div id="page-header-left-<?php echo $id; ?>">
        <div class="page-picture"></div>
    </div>
    <div id="page-header-right-<?php echo $id; ?>">
        <div><span class='page-name'><?php echo CHtml::link($post->name, array("page/$slug")); ?></span></div>
        <div><span class='page-household'></span><span class='page-population'></span></div>
        <div><span class='page-lat'></span><span class='page-lng'></span></div>
        <div><span class='page-needs'>Need : </span></div>
    </div>
</div>
<script>
(function() {
    var urlInfo = "<?php echo $this->createUrl('api/pageInfo'); ?>";
    var urlNeeds = "<?php echo $this->createUrl('api/pageNeeds'); ?>";
    var pageId = "#page-header-<?php echo $id; ?>";

    $.getJSON(urlInfo, {slug: "<?php echo $slug; ?>"}, function (resp) {
        var imgStr = '<img src="' + baseUrl + resp.picSmall + '"/>';
        $('.page-picture', pageId).html(imgStr);
        //$('.page-name', pageId).html(resp.name);
        $('.page-household', pageId).html(resp.household + ' house ');
        $('.page-population', pageId).html(resp.population + ' man');
        $('.page-lat', pageId).html('Latitude : ' + resp.lat);
        $('.page-lng', pageId).html(' Longitude : ' + resp.lng);
    });

    $.getJSON(urlNeeds, {slug: "<?php echo $slug ?>", limit: 4}, function (resp) {
        for(el in resp.needs) {
            $('.page-needs', pageId).append(' ' + resp.needs[el].message);
        }
    });
})();
</script>