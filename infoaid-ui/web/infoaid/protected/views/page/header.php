<?php
    $post = PageHelper::getJSON($slug, "info");
    $needs = PageHelper::getJSON($slug, 'limit_need/4');
    $userId = Yii::app()->user->getId();
    $isJoined = PageHelper::isJoined($userId, $slug);
?>
<div id="page-header-<?php echo $post->id; ?>" class='page-header span10'>
    <div id="page-header-left-<?php echo $post->id; ?>">
        <div class="page-picture">
            <?php 
                if($post->picSmall == null) {
                    echo '<img src='.Yii::app()->baseUrl.'/media/pages/page_default_small.jpg class="img-polaroid">';
                } else {
                    echo '<img src=' . Yii::app()->baseUrl . $post->picSmall . ' class="img-polaroid">';
                }
            ?>
        </div>
    </div>
    <div id="page-header-right-<?php echo $post->id; ?>">
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
    <div id="page-header-join-leave-<?php echo $post->id; ?>">
        <?php
            if($isJoined->isJoined == 1) {
                echo '<a class="btn" onclick="leavePage()" href="#">Leave Page</a>';
            } else {
                echo '<a class="btn" onclick="joinPage()" href="#">Join Page</a>';
            }
        ?>
        
    </div>
    <div id="join-leave-page-loading">
    </div>
</div>
<script>
    var target = document.getElementById('join-leave-page-loading');
    var spinner
    var opts = {
        lines: 13, // The number of lines to draw
        length: 7, // The length of each line
        width: 4, // The line thickness
        radius: 10, // The radius of the inner circle
        corners: 1, // Corner roundness (0..1)
        rotate: 0, // The rotation offset
        color: '#000', // #rgb or #rrggbb
        speed: 1, // Rounds per second
        trail: 60, // Afterglow percentage
        shadow: false, // Whether to render a shadow
        hwaccel: false, // Whether to use hardware acceleration
        className: 'spinner', // The CSS class to assign to the spinner
        zIndex: 2e9, // The z-index (defaults to 2000000000)
        top: 'auto', // Top position relative to parent in px
        left: 'auto' // Left position relative to parent in px
    };

    function ajaxLoading() {
        spinner = new Spinner(opts).spin(target);
    }

    function leavePage() {
        $('#page-header-join-leave'+'<?php echo $post->id; ?>').hide();
        ajaxLoading();

        $.getJSON('<?php echo $this->createUrl("page/leavePage"); ?>', {userId: '<?php echo $userId; ?>', slug: '<?php echo $slug;?>'}, function(resp) {
            console.log(resp)
            if(resp.status == 1) {
                $('#page-header-join-leave-'+'<?php echo $post->id; ?>').html('<a class="btn" onclick="joinPage()" href="#">Join Page</a>');
            } else {
            }
            $('#page-header-join-leave-'+'<?php echo $post->id; ?>').show();
            spinner.stop();
        })
    }

    function joinPage() {
        $('#page-header-join-leave'+'<?php echo $post->id; ?>').hide();
        ajaxLoading();

        $.getJSON('<?php echo $this->createUrl("page/joinPage"); ?>', {userId: '<?php echo $userId; ?>', slug: '<?php echo $slug;?>'}, function(resp) {
            if(resp.status == 1) {
                $('#page-header-join-leave-'+'<?php echo $post->id; ?>').html('<a class="btn" onclick="leavePage()" href="#">Leave Page</a>');
            } else {
            }
            $('#page-header-join-leave-'+'<?php echo $post->id; ?>').show();
            spinner.stop();
        })
    }
</script>