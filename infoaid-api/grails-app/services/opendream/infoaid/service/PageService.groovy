package opendream.infoaid.service

import opendream.infoaid.domain.Page
import opendream.infoaid.domain.Post
import opendream.infoaid.domain.Comment
import opendream.infoaid.domain.PageUser
import opendream.infoaid.domain.Users
import opendream.infoaid.domain.Need

class PageService {

    def serviceMethod() {

    }

    def getInfo(pageId) {

    	def pageInfo = Page.get(pageId)
    }

    def getPosts(pageId, offset) {

    	def posts = Post.createCriteria().list() {
    		maxResults(10)
    		firstResult(offset)
    		order('lastActived', 'desc')
    		page {
    			idEq(pageId)	
    		}
    	}

        def resultList = []

        posts.each {

            def comment = getLimitComments(it.id)

            resultList << [id: it.id, dateCreated: it.dateCreated, createdBy: it.createdBy, lastUpdated: it.lastUpdated, lastActived: it.lastActived, updateBy: it.updatedBy, comment: comment]

        }

    	[posts: resultList]
    }

    def getComments(postId) {

    	def comments = Comment.createCriteria().list() {
    		post {
    			idEq(postId)
    		}
    		order('dateCreated', 'asc')
    	}

    	[comments: comments, totalComments: comments.size()]
    }

    def getLimitComments(postId) {

        def comments = Comment.createCriteria().list(max: 3) {
            post {
                idEq(postId)
            }
            order('dateCreated', 'asc')
        }

        [comments: comments, totalComments: comments.totalCount]
    }

    def postComment(postId, message) {

    	def commentDate = new Date()
    	def post = Post.get(postId)
    	def comment = new Comment(message: message, dateCreated: commentDate)
    	post.addToComments(comment)
    	post.lastActived = commentDate
    	if(!post.save()) {
            return false
        }
    }

    def createPage(userId, name, lat, lng, location) {

        def page = new Page(name: name, lat: lat, lng: lng, location: location)
        if(!page.save()) {
            return false
        }
        def user = Users.get(userId)
        def pageUser = PageUser.createPage(user, page)

    }

    def joinPage(userId, pageId) {

        def user = Users.get(userId)
        def page = Page.get(pageId)

        def pageUser = PageUser.joinPage(user, page)
    }

    def leavePage(userId, pageId) {
        def user = Users.get(userId)
        def page = Page.get(pageId)

        def pageUser = PageUser.leavePage(user, page)
    }

    def inactivePage(userId, pageId) {
        def user = Users.get(userId)
        def page = Page.get(pageId)

        page.users.each {
            if((it.user == user) && (it.relation == PageUser.Relation.OWNER)) {
                page.status = Page.Status.INACTIVE
                if(!page.save()) {
                    return false
                }
            }
        }
    }

    def getAllNeeds(pageId) {
        def page = Page.get(pageId)
        def needs = Need.findAllByPageAndStatus(page, Post.Status.ACTIVE)
        [needs: needs, totalNeeds: needs.size()]
    }

    def getLimitNeeds(pageId) {

    }

    def getMembers(pageId) {
        def page = Page.get(pageId)

        page.users
    }

    def createNeed(pageId, message) {
        def page = Page.get(pageId)
        def date = new Date()
        def need = new Need(lastActived: date, createdBy: 'nut', updatedBy: 'nut', expiredDate: date, message: message, quantity: 10)
        page.addToPosts(need)
        if(!page.save()) {
            return false
        }
    }


}
