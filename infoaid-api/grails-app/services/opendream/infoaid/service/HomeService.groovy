package opendream.infoaid.service

import opendream.infoaid.domain.Post

class HomeService {

    def getFeedByLastActived(user, offset = 0, max = 10) {

        /*def posts = Post.createCriteria().list() {            
            in(page, user.pages)            
            order('lastActived', 'desc')
        }*/
        
        
        def posts = Post.findAll(max: max, sort:"lastActived", order : 'desc', offset: offset) {
            page in user.pages
        }

        //posts = query.list()        

        //[posts: posts, totalPosts: posts.totalCount]
        posts
    }
}
