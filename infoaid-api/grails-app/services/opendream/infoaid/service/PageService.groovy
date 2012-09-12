package opendream.infoaid.service

import opendream.infoaid.domain.Page
import opendream.infoaid.domain.Post
import opendream.infoaid.domain.Comment
import opendream.infoaid.domain.Item
import opendream.infoaid.domain.PageUser
import opendream.infoaid.domain.User
import opendream.infoaid.domain.Need
import opendream.infoaid.domain.MessagePost

class PageService {

    def getInfo(slug) {
    	def pageInfo = Page.findBySlug(slug)
    }

    def getPosts(slug, fromId=null, toId=null, since=null, until=null, max = 10, type = null) {
        def posts = Post.createCriteria().list() {
            eq('status', Post.Status.ACTIVE)
            page {
                eq('slug', slug)
            }
            maxResults(max)
            if(fromId) {
                ge('id', fromId)
            }
            if(toId) {
                le('id', toId)
            }
            if(since) {
                ge('dateCreated', since)
            }
            if(until) {
                le('dateCreated', until)
            }
            if(type == 'top') {
                order('conversation', 'desc')
            }
            order('lastActived', 'desc')
        }
        return posts
    }

    def getTopPost(slug, fromId=null, toId=null, since=null, until=null, max = 10) {
        getPosts(slug, fromId, toId, since, until, max, 'top')
    }

    def getRecentPost(slug, fromId=null, toId=null, since=null, until=null, max = 10) {
        getPosts(slug, fromId, toId, since, until, max, 'recent')
    }

    def getComments(postId, fromId=null, toId=null, since=null, until=null) {
        def max = 50
    	def comments = Comment.createCriteria().list(max: max) {
    		post {
    			idEq(postId)
    		}
            if(fromId) {
                ge('id', fromId)
            }
            if(toId) {
                le('id', toId)
            }
            if(since) {
                ge('dateCreated', since)
            }
            if(until) {
                le('dateCreated', until)
            }
    		order('dateCreated', 'asc')
    	}

    	[comments: comments, totalComments: comments.totalCount]
    }

    def getLimitComments(postId) {
        def comments = Post.findById(postId).previewComments
    }

    def postComment(userId, postId, message) {
        def user = User.get(userId)
    	def commentDate = new Date()
    	def post = Post.get(postId)
        
    	def comment = new Comment(message: message, dateCreated: commentDate, user: user)
    	post.addToComments(comment)
    	post.lastActived = commentDate
        post.conversation++

        post.save(failOnError: true, flush:true)

        def pageUser = PageUser.get(user.id, post.page.id)
        pageUser.conversation++
        pageUser.save()
        [user: user, post: post, comment: comment]
    }

    def createPage(userId, name, lat, lng, location, household, population, about, picOriginal) {
        def page = new Page(name: name, lat: lat, lng: lng, location: location,
            household: household, population: population, about: about, picOriginal: picOriginal)
        page.save(failOnError: true)
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
        def needs = Need.createCriteria().list(max: max, sort: 'dateCreated', order: 'desc', cache: true) {
            eq('status', Post.Status.ACTIVE)
            eq('page', page)
        }

        /*def needs = Need.findAll("from Need as n where n.status = :status and page.slug = :slug \
                                order by n.dateCreated desc ",
                                [status: Post.Status.ACTIVE, slug: slug] )*/

        [needs: needs, totalNeeds: needs.totalCount]
    }

    def getMembers(slug) {
        def page = Page.findBySlug(slug)
        page.users
    }

    def getTopMembers(slug) {
        def page = Page.findBySlug(slug)
        def pageUsers = PageUser.createCriteria().list(sort: 'conversation', order: 'desc', max: 5) {
            eq('page', page)
        }

        //pageUser.collect { it.user }
        [page:page, pageUsers:pageUsers]
    }

    def createNeed(userId, slug, itemId, quantity, message = "") {
        def user = User.get(userId)
        def page = Page.findBySlug(slug)
        def item = Item.get(itemId)
        def pageUser = PageUser.findByUserAndPage(user, page)
        pageUser.conversation++
        pageUser.save()
        def date = new Date()
        def need = new Need(lastActived: date, createdBy: user.username, updatedBy: user.username, expiredDate: date, message: message, item: item, quantity: quantity)
        page.addToPosts(need)
        page.save(failOnError: true, flush: true)
        return [user: user, page: page, post: need]
    }

    def createMessagePost(userId, slug, message) {
        def user = User.get(userId)
        def page = Page.findBySlug(slug)
        def pageUser = PageUser.findByUserAndPage(user, page)
        if(pageUser) {
            pageUser.conversation++
            pageUser.save()
        }
        def date = new Date()
        def messagePost = new MessagePost(lastActived: date, createdBy: user.username, updatedBy: user.username, expiredDate: date+14, message: message)
        page.addToPosts(messagePost)
        page.save(failOnError: true, flush: true)
        return [user: user, page: page, post: messagePost]
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
        page.properties['name', 'lat', 'lng', 'location', 'status', 'household', 'population', 'about', 'version', 'picOriginal'] = data
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

    def enablePage(slug) {
        def page = Page.findBySlug(slug)
        if(!page) {
            return
        }

        page.status = Page.Status.ACTIVE
        if(!page.save()) {
            return
        }
        page
    }

    def getActiveNeedPage() {
        def pages = Need.createCriteria().list() {            
            eq('status', Post.Status.ACTIVE)
            
            projections {
                distinct('page')
            }
        }
        pages
    }

    def searchPage(word = null) {
        def max = 10
        def pages = Page.createCriteria().list(max: max, sort: 'name', order: 'asc') {
            eq('status', Page.Status.ACTIVE)
            or {
                ilike("name", "%${word}%")
                ilike("about", "%${word}%")
            }
        }
        pages
    }
}
