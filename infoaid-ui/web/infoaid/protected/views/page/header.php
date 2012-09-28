<?php
    $post = PageHelper::getInfoBySlug($slug);
    $needs = PageHelper::getJSON($slug, 'limit_need/4');
    $userId = Yii::app()->user->getId();
    $isJoined = PageHelper::isJoined($userId, $slug);
?>
<div id="page-header-<?php echo $post->id; ?>" class='page-header span10' ng-controller="HeaderCtrl">
    <div id="page-header-left-<?php echo $post->id; ?>"  
        ng-init="slug='<?php echo $slug; ?>'; isjoined='<?php echo $isJoined->isJoined; ?>'; 
        userId='<?php echo $userId; ?>'; pageId='<?php echo $post->id; ?>'">
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
        <button class='btn' ng-click="handleClick()" ng-show="userId">{{isjoined | JoinLabel}}</button>        
    </div>
    <div id="join-leave-page-loading-<?php echo $post->id?>"></div>    
</div>