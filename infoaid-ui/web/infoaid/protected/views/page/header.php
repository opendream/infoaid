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
    var imgStr = ''
    $.getJSON(urlInfo, {slug: "<?php echo $slug; ?>"}, function (resp) {
        
        if(resp.picSmall != null) {
            imgStr = '<img src="' + baseUrl + resp.picSmall + '"/>';
        } else {
            imgStr = '<img src="' + baseUrl + '/media/pages/page_default_small.jpg"/>';
        }

        if(resp.household != null) {
            $('.page-household', pageId).html(resp.household + ' house ');
        } else {
            $('.page-household', pageId).html(' ? house ');
        }

        if(resp.population != null) {
            $('.page-population', pageId).html(resp.population + ' man');
        } else {
            $('.page-population', pageId).html(' ? man');
        }
        
        $('.page-picture', pageId).html(imgStr);
        
        
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