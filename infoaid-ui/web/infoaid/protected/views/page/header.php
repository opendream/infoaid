<?php
    $post = PageHelper::getJSON($slug, "info");
    $needs = PageHelper::getJSON($slug, 'limit_need/4');
?>
<div id="page-header-<?php echo $id; ?>" class='page-header'>
    <div id="page-header-left-<?php echo $id; ?>">
        <div class="page-picture">
            <?php 
                if($post->picSmall == null) {
                    echo '<img src="/infoaid/media/pages/page_default_small.jpg" />';
                } else {
                    echo '<img src=' . Yii::app()->baseUrl . $post->picSmall . '>';
                }
            ?>
        </div>
    </div>
    <div id="page-header-right-<?php echo $id; ?>">
        <div><span class='page-name'><?php echo CHtml::link($post->name, array("page/$slug")); ?></span></div>
        <div class='page-household-population'>
            <span>
                <?php
                    if($post->household == null) {
                        echo '? House';
                    } else {
                        echo $post->household.' House';
                    }
                ?>
            </span>
            <span>
                <?php
                    if($post->population == null) {
                        echo '? Man';
                    } else {
                        echo $post->population.' Man';
                    }
                ?>
            </span>
        </div>
        <div class='page-lat-lng'>
            <span>
                <?php
                    if($post->lat == null) {
                        echo '<b>Latitude :</b> ?';
                    } else {
                        echo '<b>Latitude :</b> '.$post->lat;
                    }
                ?>
            </span>
            <span>
                <?php
                    if($post->lng == null) {
                        echo '<b>Longitude :</b> ?';
                    } else {
                        echo '<b>Longitude :</b> '.$post->lng;
                    }
                ?>
            </span>
        </div>
        <div>
            <span class='page-needs'>
                <b>Need :</b>
                    <?php
                        if($needs->status == 0 || $needs->needs == null) {
                            echo ' ?';
                        } else {
                            foreach($needs->needs as $el) {
                                echo ' '.$el->message.' ';
                            }    
                        }
                    ?>
            </span>
        </div>
    </div>
</div>