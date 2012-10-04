package opendream.infoaid.service

import opendream.infoaid.domain.Page
import opendream.infoaid.domain.Post
import opendream.infoaid.domain.Comment
import opendream.infoaid.domain.Item
import opendream.infoaid.domain.PageUser
import opendream.infoaid.domain.User
import opendream.infoaid.domain.Need
import opendream.infoaid.domain.MessagePost
import opendream.infoaid.domain.Resource

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class PageService {
    def grailsApplication

    def getInfo(slug) {
        def pageInfo = Page.findBySlug(slug)
    }

    def getPosts(slug, fromId=null, toId=null, since=null, until=null, max=null, type = null) {
        max = max?:grailsApplication.config.infoaid.api.post.max
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

    def getPageAuthority(user, slug) {
        def author = [:]
        author.isJoined = user instanceof String? false:isJoined(user.id, slug).isJoined
        author.isOwner = user instanceof String? false:isOwner(user.id, slug).isOwner
        
        author
    }

    def getPostAuthority(user, postId) {
        def post = Post.get(postId)
        def author = [:]
        author.isJoined = user instanceof String? false:isJoined(user.id, post?.page?.slug).isJoined
        author.isOwner = user instanceof String? false:isOwner(user.id, post?.page?.slug).isOwner
        
        author
    }

    def canDelete(userId, currentUser, isOwner) {
        if(currentUser instanceof String) {
            return false
        } else if(isOwner==true) {
            return true
        }else {
            return userId == currentUser.id
        }
    }

    def getTopPost(user, slug, fromId=null, toId=null, since=null, until=null, max=null) {
        max = max?:grailsApplication.config.infoaid.api.post.max
        [posts: getPosts(slug, fromId, toId, since, until, max, 'top'),
        author: getPageAuthority(user, slug)]
    }

    def getRecentPost(user, slug, fromId=null, toId=null, since=null, until=null, max=null) {
        max = max?:grailsApplication.config.infoaid.api.post.max
        [posts: getPosts(slug, fromId, toId, since, until, max, 'recent'), 
        author: getPageAuthority(user, slug)]
    }

    def getComments(user, postId, fromId=null, toId=null, since=null, until=null) {
        def max = grailsApplication.config.infoaid.api.comment.limited
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
            eq('enabled', true)
    		order('dateCreated', 'asc')
    	}

    	[comments: comments, totalComments: comments.totalCount, author:getPostAuthority(user, postId)]

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

    def createPage(userId, name, lat, lng, location, household, population, about, picOriginal, picSmall, picLarge) {
        def page = new Page(name: name, lat: lat, lng: lng, location: location,
            household: household, population: population, about: about, 
            picOriginal: picOriginal, picSmall: picSmall, picLarge: picLarge)
        try {
            page.save(failOnError: true, flush: true)
        }
        catch (e) {
            log.error e
            throw e
        }
        def user = User.get(userId)
        PageUser.createPage(user, page)
        page
    }

    def joinPage(userId, slug) {
        def user = User.get(userId)
        def page = Page.findBySlug(slug)
        def isJoined = isJoined(userId, slug)
        if(isJoined['isJoined']) {
            return [message: 'Already joined this page']
        } else {
            try{
                PageUser.joinPage(user, page)    
            } catch (e) {
                log.error e
                throw e
            }
        }
        
        
    }

    def leavePage(userId, slug) {
        def user = User.get(userId)
        def page = Page.findBySlug(slug)
        def isJoined = isJoined(userId, slug)
        if(!isJoined['isJoined']) {
            return [message: 'Already leaved this page']
        } else {
            try {
                PageUser.leavePage(user, page)
            } catch (e) {
                log.error e
                throw e
            }
        }
    }

    def removeUserFromPage(userId, slug) {
        def user = User.get(userId)
        def page = Page.findBySlug(slug)
        try {
            PageUser.removeUserFromPage(user, page)
        } catch (e) {
            log.error e
            throw e
        }
    }

    def setRelation(userId, slug, relation) {
        def user = User.get(userId)
        def page = Page.findBySlug(slug)
        try {
            PageUser.setRelation(user, page, relation)
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
        max = max?:grailsApplication.config.infoaid.api.need.max
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

    def getMembers(slug, offset, max) {
        def page = Page.findBySlug(slug)
        page.getUsers(offset, max)
    }

    def getTopMembers(slug) {
        def max = grailsApplication.config.infoaid.api.member.max
        def page = Page.findBySlug(slug)
        def pageUsers = PageUser.createCriteria().list(sort: 'conversation', order: 'desc', max: max) {
            eq('page', page)
        }

        //pageUser.collect { it.user }
        [page:page, pageUsers:pageUsers]
    }

    def isOwner(userId, slug) {
        def user = User.get(userId)
        def page = Page.findBySlug(slug)

        def pageUser = PageUser.findByUserAndPage(user, page)
        if(!pageUser) {
            [isOwner: false]
        } else {
            if(pageUser.relation == PageUser.Relation.OWNER) {
                [isOwner: true]
            } else {
                [isOwner: false]
            }
        }
        
    }

    def isJoined(userId, slug) {
        def user = User.get(userId)
        def page = Page.findBySlug(slug)

        def pageUser = PageUser.findByUserAndPage(user, page)
        if(!pageUser) {
            [isJoined: false]
        } else {
            [isJoined: true]
        }
    }

    def createNeed(userId, slug, itemId, quantity, message = "") {
        def user = User.get(userId)
        def page = Page.findBySlug(slug)
        def item = Item.get(itemId)
        def pageUser = PageUser.findByUserAndPage(user, page)
        if(pageUser) {
            pageUser.conversation++
            pageUser.save()
        }
        def date = new Date()
        def need = new Need(lastActived: date, createdBy: user, updatedBy: user, expiredDate: date+14, message: message, item: item, quantity: quantity)
        page.addToPosts(need)
        page.save(failOnError: true, flush: true)
        return [user: user, page: page, post: need]
    }

    def createMessagePost(userId, slug, message, picOriginal, picSmall) {
        def user = User.get(userId)
        def page = Page.findBySlug(slug)
        def pageUser = PageUser.findByUserAndPage(user, page)
        if(pageUser) {
            def date = new Date()
            def messagePost = new MessagePost(lastActived: date, createdBy: user, updatedBy: user, expiredDate: date+14, message: message, 
                picOriginal: picOriginal, picSmall: picSmall)
            page.addToPosts(messagePost)
            if(page.save(failOnError: true, flush: true)) {
                pageUser.conversation++
                pageUser.save()
                return [user: user, page: page, post: messagePost]
            }
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
            return [status: 0, message: 'Page not found', data: data]
        }
        if(data.version) {
            def version = data.version.toLong()
            if (page.version > version) {
                page.errors.rejectValue("version", "default.optimistic.locking.failure")
                return [status: 0, message: "Another user has updated this Page while you were editing", data: data]
            }
        }
        page.properties['name', 'lat', 'lng', 'location', 'status', 'household', 'population', 'about', 'version', 
        'picOriginal', 'picSmall', 'picLarge'] = data
        if(!page.save()) {
            return [status: 0, message: 'Cannot update this page', data: data]
        }
        return [status: 1, message: 'This page was updated', data: data]
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

    def searchPage(word = null, offset = 0) {
        def max = ConfigurationHolder.config.infoaid.api.getResource.max
        def offsetInt = offset.toInteger()
        def pages
        if(!word) {
            pages = Page.createCriteria().list(max: max, sort: 'dateCreated', order: 'desc', offset: offsetInt) {
                eq('status', Page.Status.ACTIVE)
                or {
                    ilike("name", "%${word}%")
                    ilike("about", "%${word}%")
                }
            }
        } else {
            pages = Page.createCriteria().list(max: max, sort: 'name', order: 'asc', offset: offsetInt) {
                eq('status', Page.Status.ACTIVE)
                or {
                    ilike("name", "%${word}%")
                    ilike("about", "%${word}%")
                }
            }
        }
        pages
    }

    def disableComment(userId, commentId) {
        def comment = Comment.get(commentId)
        if(comment?.user?.id != userId) {
            [status:0, message:'unauthorized user or not found comment']
        } else {
            comment.enabled = false
            comment.save(failOnError: true, flush:true)
            def post = comment.post
            post.conversation--
            post.save(failOnError: true, flush:true)
            [status:1, message:"comment ${commentId} is deleted", id:commentId]
        } 
    }

    def disablePost(userId, postId) {    
        def post = Post.get(postId)  
        if(post?.createdBy?.id != userId) {
            [status:0, message:'unauthorized user or not found post']
        } else {  
            post.status = Post.Status.INACTIVE
            post.save(failOnError: true, flush:true) 
            [status:1, message:"post ${postId} is deleted", id:postId] 
        }
    }

    def getResource(params) {
        def max = params.max ? params.max : ConfigurationHolder.config.infoaid.api.getResource.max
        params.order = params.order ? params.order : 'asc'
        def ret = [:]
        ret.status = 0
        def resources = Resource.createCriteria().list() {
            maxResults(max)
            if(params.slug) {
                page {
                    eq('slug', params.slug)
                }
            }
            if(params.userId) {
                createdBy {
                    eq('id', params.userId)
                }
            }
            if(params.itemName) {
                item {
                    eq('name', params.itemName)
                }
            }
            if(params.fromId) {
                ge('id', params.fromId)
            }
            if(params.toId) {
                le('id', params.toId)
            }
            if(params.since) {
                ge('dateCreated', params.since)
            }
            if(params.until) {
                le('dateCreated', params.until)
            }
            if(params.sort) {
                order(params.sort, params.order)
            }
        }
        ret = [status: 1, resources: resources, totalResources: resources.size()]

        return ret
    }

    def createResource(params) {
        def ret = [:]
        ret.status = 0
        
        def quantity = params.quantity ? params.quantity : 0
        def message = params.message

        def user = User.get(params.userId)
        if(!user) {
            ret.message = 'User Id not found'
            return ret
        }

        if(!params.slug) {
            ret.message = 'Page not found'
            return ret
        }
        def page = Page.findBySlug(params.slug)
        if(!page) {
            ret.message = 'Page not found'
            return ret
        }

        def item = Item.get(params.itemId)
        if(!item) {
            ret.message = 'Item not found'
            return ret
        }

        def pageUser = PageUser.findByUserAndPage(user, page)
        if(!pageUser) {
            ret.message = 'Please join this page'
            return ret
        }
        pageUser.conversation++
        pageUser.save()

        def date = new Date()
        def previousSumQuantity = 0
        Resource.findAllByItemAndPageAndStatus(item, page, Post.Status.ACTIVE).each {
            previousSumQuantity += it.quantity
        }

        def resource = new Resource(page: page, lastActived: date, createdBy: user, 
            updatedBy: user, expiredDate: date+14, message: message, item: item, 
            quantity: quantity, previousSumQuantity: previousSumQuantity)
        page.addToPosts(resource)
        if(!page.save(flush: true)) {
            ret.message = 'Error, Can not save this resource'
            return ret
        }
        ret.status = 1
        ret.user = user
        ret.page = page
        ret.post = resource
        ret.post.previousSumQuantity = previousSumQuantity
        ret.pageUser = pageUser

        return ret
    }

    def getItemSummary(page) {
        Item.list().collect { item ->
            // Find sum of each item

            // Both Need and Resource share same criteria set.
            def criteria = {
                eq("page", page)
                eq("status", Post.Status.ACTIVE)
                eq("item", item)
                projections {
                    sum("quantity")
                }
            }

            def sumResource = Resource.createCriteria().get(criteria) ?: 0
            def sumNeed = Need.createCriteria().get(criteria) ?: 0
            // Ugly workaround
            sumNeed -= sumResource

            [
                name: item.name,
                need: sumNeed,
                resource: sumResource,
            ]
        }.findAll { it.need || it.resource }
    }
}
