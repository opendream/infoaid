package opendream.infoaid.service

import opendream.infoaid.domain.Post
import opendream.infoaid.domain.Need

class TimeboxingService {

    def disableExpiredNeed(now=new Date()) {
        def needs = Need.findAllByStatus(Post.Status.ACTIVE)
        needs.each {
            if(it.expiredDate <= now) {
                it.status = Post.Status.INACTIVE
                it.save(failOnError:true, flush:true)
            }        
        }
    }
}
