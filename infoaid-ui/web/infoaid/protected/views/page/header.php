<div id='page-header' class='page-header'>
    <div id='page-header-left'>
        <div id='page-picture' class='pic-small'></div>
    </div>
    <div id='page-header-right'>
        <div><span id='page-name' class='word-original'></span></div>
        <div><span id='page-household' class='word-original'></span><span id='page-population' class='word-original'></span></div>
        <div><span id='page-lat' class='page-lat'></span><span id='page-lng' class='page-lng'></span></div>
        <div><span id='page-needs' class='page-needs'>Need : </span></div>
    </div>
</div>
<script>
    var urlInfo = "<?php echo $this->createUrl('api/pageInfo'); ?>";
    var urlNeeds = "<?php echo $this->createUrl('api/pageNeeds'); ?>";
    $.getJSON(urlInfo, {slug: "<?php echo $slug ?>"}, function (resp) {
        var imgStr = '<img src="' + baseUrl + resp.picSmall + '"/>';
        $('#page-picture').html(imgStr);
        $('#page-name').html(resp.name);
        $('#page-household').html(resp.household + ' house ');
        $('#page-population').html(resp.population + ' man');
        $('#page-lat').html('Latitude : ' + resp.lat);
        $('#page-lng').html(' Longitude : ' + resp.lng);
    });

    $.getJSON(urlNeeds, {slug: "<?php echo $slug ?>", limit: 4}, function (resp) {
        for(el in resp.needs) {
            $('#page-needs').append(' ' + resp.needs[el].message);
        }
    });
</script>