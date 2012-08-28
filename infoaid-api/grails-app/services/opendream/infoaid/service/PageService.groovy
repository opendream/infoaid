package opendream.infoaid.service

import opendream.infoaid.domain.Page
import opendream.infoaid.domain.Post
import opendream.infoaid.domain.Comment
import opendream.infoaid.domain.PageUser
import opendream.infoaid.domain.Users
import opendream.infoaid.domain.Need
import opendream.infoaid.domain.MessagePost

class PageService {

    def serviceMethod() {

    }

    def getInfo(slug) {

    	def pageInfo = Page.findBySlug(slug)
    }

    def getPosts(slug, offset, max) {
    	def posts = Post.createCriteria().list(max: max, sort: 'lastActived', order: 'desc', offset: offset) {
            eq('status', Post.Status.ACTIVE)
    		page {
                eq('slug', slug)
    		}
    	}
        
    	[posts: posts, totalPosts: posts.totalCount]
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

    def getLimitComments(postId, max) {

        def comments = Comment.createCriteria().list(max: max) {
            post {
                idEq(postId)
            }
            order('dateCreated', 'asc')
        }

        [comments: comments, totalComments: comments.totalCount]
    }

    def postComment(userId, postId, message) {

        def user = Users.get(userId)
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
        PageUser.createPage(user, page)

    }

    def joinPage(userId, pageId) {

        def user = Users.get(userId)
        def page = Page.get(pageId)

        PageUser.joinPage(user, page)
    }

    def leavePage(userId, pageId) {
        def user = Users.get(userId)
        def page = Page.get(pageId)

        PageUser.leavePage(user, page)
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

    def getLimitNeeds(pageId, max) {
        def page = Page.get(pageId)
        def needs = Need.createCriteria().list(max: max) {
            eq('status', Post.Status.ACTIVE)
            eq('page', page)
        }

        [needs: needs, totalNeeds: needs.totalCount]
    }

    def getMembers(slug) {
        def page = Page.findBySlug(slug)
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

    def createMessagePost(pageId, message) {
        def page = Page.get(pageId)
        def date = new Date()
        def messagePost = new MessagePost(lastActived: date, createdBy: 'nut', updatedBy: 'nut', expiredDate: date, message: message)
        page.addToPosts(messagePost)
        if(!page.save()) {
            return false
        }
    }


}
