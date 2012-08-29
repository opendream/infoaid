package opendream.infoaid.service

import opendream.infoaid.domain.Post

class HomeService {

    def getFeedByRecentPost(user, offset = 0, max = 10) {
        def posts = Post.findAll(max: max, sort:"lastActived", order : 'desc', offset: offset) {
            page in user.pages
        }        
        posts
    }

    def getFeedByTopPost(user, offset = 0, max = 10) {
        def posts = Post.findAll(max: max, sort:"conversation", order : 'desc', offset: offset) {
            page in user.pages
        }        
        posts
    }
}
