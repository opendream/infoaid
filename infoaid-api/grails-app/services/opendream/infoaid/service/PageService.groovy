package opendream.infoaid.service

import opendream.infoaid.domain.Page
import opendream.infoaid.domain.Post
import opendream.infoaid.domain.Comment
import opendream.infoaid.domain.PageUser
import opendream.infoaid.domain.User
import opendream.infoaid.domain.Need
import opendream.infoaid.domain.MessagePost

class PageService {

    def getInfo(slug) {

    	def pageInfo = Page.findBySlug(slug)
    }

    def getPosts(slug, offset = 0, max = 10, order = 'lastActived desc') {
    	Post.findAll ("from Post where status =:status and page.slug =:slug \
                        order by $order",
                        [status: Post.Status.ACTIVE, slug: slug, max: max, offset: offset])
    }

    def getTopPost(slug, offset) {
        def max = 10 // config file
        def order = 'conversation desc, lastActived desc' // config file
        getPosts(slug, offset, max, order)        
    }

    def getRecentPost(slug, offset) {
        def max = 10 // config file
        def order = 'lastActived desc' // config file
        getPosts(slug, offset, max, order)        
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

        def user = User.get(userId)
    	def commentDate = new Date()
    	def post = Post.get(postId)
        
    	def comment = new Comment(message: message, dateCreated: commentDate)
    	post.addToComments(comment)
    	post.lastActived = commentDate
        post.conversation++

        if(!post.save(flush:true)) {
            return false
        }

        def pageUser = PageUser.get(user.id, post.page.id)
        pageUser.conversation++
        pageUser.save()
    }

    def createPage(userId, name, lat, lng, location, household, population, about) {
        def page = new Page(name: name, lat: lat, lng: lng, location: location,
            household: household, population: population, about: about)
        if(!page.save()) {
            //throw new RuntimeException("can not save new page")
            return false
        }
        def user = User.get(userId)
        PageUser.createPage(user, page)
        page
    }

    def joinPage(userId, slug) {

        def user = User.get(userId)
        def page = Page.findBySlug(slug)

        PageUser.joinPage(user, page)
    }

    def leavePage(userId, slug) {
        def user = User.get(userId)
        def page = Page.findBySlug(slug)
        try {
            PageUser.leavePage(user, page)
        } catch (e) {
            log.error e
            throw e
        }
    }

    def inactivePage(userId, slug) {
        def user = User.get(userId)
        def page = Page.findBySlug(slug)

        page.users.each {
            if((it.user == user) && (it.relation == PageUser.Relation.OWNER)) {
                page.status = Page.Status.INACTIVE
                if(!page.save()) {
                    return false
                }
            }
        }
    }

    def getAllNeeds(slug) {
        def page = Page.findBySlug(slug)
        def needs = Need.findAllByPageAndStatus(page, Post.Status.ACTIVE)

        [needs: needs, totalNeeds: needs.size()]
    }

    def getLimitNeeds(slug, max) {
        def page = Page.findBySlug(slug)
        def needs = Need.createCriteria().list(max: max, sort: 'dateCreated', order: 'desc') {
            eq('status', Post.Status.ACTIVE)
            eq('page', page)
        }

        [needs: needs, totalNeeds: needs.totalCount]
    }

    def getMembers(slug) {
        def page = Page.findBySlug(slug)
        page.users
    }

    def getTopMembers(slug) {
        def page = Page.findBySlug(slug)
        def pageUser = PageUser.createCriteria().list(sort: 'conversation', order: 'desc', max: 5) {
            eq('page', page)
        }

        pageUser.user
    }

    def createNeed(userId, slug, message) {
        def user = User.get(userId)
        def page = Page.findBySlug(slug)
        def pageUser = PageUser.findByUserAndPage(user, page)
        pageUser.conversation++
        pageUser.save()
        def date = new Date()
        def need = new Need(lastActived: date, createdBy: 'nut', updatedBy: 'nut', expiredDate: date, message: message, quantity: 10)
        page.addToPosts(need)
        if(!page.save()) {
            return false
        }
    }

    def createMessagePost(userId, slug, message) {
        def user = User.get(userId)
        def page = Page.findBySlug(slug)
        def pageUser = PageUser.findByUserAndPage(user, page)
        pageUser.conversation++
        pageUser.save()
        def date = new Date()
        def messagePost = new MessagePost(lastActived: date, createdBy: 'nut', updatedBy: 'nut', expiredDate: date, message: message)
        page.addToPosts(messagePost)
        if(!page.save()) {
            return false
        }
    }

    def getAbout(slug) {
        def page = Page.findBySlug(slug)
        page.about
    }

    def getSummaryInfo() {
        Page.findAllByStatus(Page.Status.ACTIVE)
    }

    def updatePage(slug, data) {
        def page = Page.findBySlug(slug)
        if(!page) {
            return
        }
        if(data.version) {
            def version = data.version.toLong()
            if (page.version > version) {
                page.errors.rejectValue("version", "default.optimistic.locking.failure")
                return "Another user has updated this Page while you were editing"
            }
        }
        page.properties['name', 'lat', 'lng', 'location', 'status', 'household', 'population', 'about', 'version'] = data
        if(!page.save()) {
            return
        }
    }

    def disablePage(slug) {
        def page = Page.findBySlug(slug)
        if(!page) { // page not found
            return
        }

        page.status = Page.Status.INACTIVE
        if(!page.save()) { // process not complete
            return
        }
        page
    }
}
