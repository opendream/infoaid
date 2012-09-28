<?php
    $post = PageHelper::getInfoBySlug($slug);
    $needs = PageHelper::getJSON($slug, 'limit_need/4');
    $userId = Yii::app()->user->getId();
    $isJoined = PageHelper::isJoined($userId, $slug);
?>
<div id="page-header-<?php echo $post->id; ?>" class='page-header span10'>
    <div id="page-header-left-<?php echo $post->id; ?>">
        <div class="page-picture">
            <?php 
                if($post->picSmall == null) {
                    echo '<a href="'.Yii::app()->baseUrl.'/page/'.$slug.'"><img src='.Yii::app()->baseUrl.'/media/pages/page_default_small.jpg class="img-polaroid"></a>';
                } else {
                    echo '<a href="'.Yii::app()->baseUrl.'/page/'.$slug.'"><img src=' . Yii::app()->baseUrl . $post->picSmall . ' class="img-polaroid"></a>';
                }
            ?>
        </div>
    </div>
    <div id="page-header-right-<?php echo $post->id; ?>" class="page-header-right">
        <div>
            <span class='page-name'>
                <?php echo CHtml::link($post->name, array("page/$slug")); ?>
            </span>
            <?php if (PageHelper::isOwner($userId, $slug)->isOwner == 1): ?>
                --
                <span class="edit">
                    <?php echo l("Edit", "/page/$slug/edit"); ?>
                </span>
            <?php endif; ?>
        </div>
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
    
    <div id="page-header-join-leave-<?php echo $post->id; ?>" class="page-header-join-leave">
        <?php
            if($userId != null) {
                if($isJoined->isJoined == 1) {
                    echo "<button class='btn' onclick=\"leavePage("."'".$slug."',".$post->id.")\" >Leave Page</button>";
                } else {
                    echo "<button class='btn' onclick=\"joinPage("."'".$slug."',".$post->id.")\" >Join Page</button>";
                }
            }
        ?>
        
    </div>
    <div id="join-leave-page-loading-<?php echo $post->id?>"></div>
</div>
<script>
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

    function ajaxLoading(pageId) {
        $('#page-header-join-leave-'+pageId).hide();
        var target = document.getElementById('join-leave-page-loading-'+pageId);
        spinner = new Spinner(opts).spin(target);
    }

    function leavePage(slug, pageId) {
        ajaxLoading(pageId);

        $.getJSON('<?php echo $this->createUrl("page/leavePage"); ?>', {userId: '<?php echo $userId; ?>', slug: slug}, function(resp) {
            if(resp.status == 1) {
                $('#page-header-join-leave-'+pageId).html('<button class="btn" onclick="joinPage('+"'"+slug+"'"+","+pageId+')">Join Page</button>');
            } else {
            }
            $('#page-header-join-leave-'+pageId).show();
            spinner.stop();
        })
    }

    function joinPage(slug, pageId) {
        ajaxLoading(pageId);

        $.getJSON('<?php echo $this->createUrl("page/joinPage"); ?>', {userId: '<?php echo $userId; ?>', slug: slug}, function(resp) {
            if(resp.status == 1) {
                $('#page-header-join-leave-'+pageId).html('<button class="btn" onclick="leavePage('+"'"+slug+"'"+","+pageId+')">Leave Page</button>');
            } else {
            }
            $('#page-header-join-leave-'+pageId).show();
            spinner.stop();
        })
    }
</script>