package opendream.infoaid.controller

import grails.converters.JSON
import org.codehaus.groovy.grails.commons.ConfigurationHolder

import opendream.infoaid.domain.Need
import opendream.infoaid.domain.Resource

class PageController {
    def pageService
    def itemService
    def springSecurityService

    def index() { }

    def info() {
        def user = params.user
        def ret = [:]
        ret.status = 0
        def info = pageService.getInfo(params.slug)
        if(info) {
            ret = [
                status: 1,
                page: info,
                items: pageService.getItemSummary(info),
                isJoined: user instanceof String? false:pageService.isJoined(user.id, info.slug).isJoined,
                isOwner: user instanceof String? false:pageService.isOwner(user.id, info.slug).isOwner
            ]
        }
        
        render ret as JSON
        
    }

    def map() {
        def ret = [:]
        def map = pageService.getInfo(params.slug)
        if(map) {
            ret = [status:1, id: map.id, name: map.name, lat: map.lat, lng: map.lng, slug: map.slug]
        }

        render ret as JSON
    }

    def member() {
        def ret = [:]
        ret.status = 0
        def offset = params.offset ? params.offset : 0
        def max = params.max ? params.max : ConfigurationHolder.config.infoaid.api.allmember.limited
        def results = pageService.getMembers(params.slug, offset, max)
        
        ret.members = results.collect{
            [
                id: it.user.id,
                username: it.user.username,
                firstname: it.user.firstname,
                lastname: it.user.lastname,
                email: it.user.email,
                telNo: it.user.telNo,
                relation: it.relation.toString(),
                picOriginal: it.user.picOriginal,
                picLarge: it.user.picLarge,
                picSmall: it.user.picSmall,
            ]
        }
        ret.status = 1
        ret.totalMembers = results.size()
        render ret as JSON
    }

    def topMember() {
        def ret = [:]
        ret.status = 0
        def results = pageService.getTopMembers(params.slug)
        ret.topMembers = results.pageUsers.collect {
            [   
                id: it.user.id,
                username: it.user.username,
                firstname: it.user.firstname,
                lastname: it.user.lastname,
                email: it.user.email,
                telNo: it.user.telNo,
                relation: it.relation.toString(),
                picOriginal: it.user.picOriginal,
                picLarge: it.user.picLarge,
                picSmall: it.user.picSmall
            ]
        }
        ret.totalTopMembers = results.pageUsers.size()
        ret.status = 1
        ret.message = "top member on page ${results.page.name}"
        render ret as JSON
    }

    def status() {
        def ret = [:]
        def since
        def until
        ret.status = 0
        if(!params.max) {
            params.max = 10
        }
        if(params.since) {
            since = new Date().parse("yyyy-MM-dd HH:mm", params.since)
        }
        if(params.until) {
            until = new Date().parse("yyyy-MM-dd HH:mm", params.until)
        }
        def posts = pageService.getPosts(params.slug, params.fromId, params.toId, since, until, params.max, params.type=null)
        if(posts) {
            ret.posts = posts.collect{
                [
                    message: it.message,
                    dateCreated: it.dateCreated.format('yyyy-MM-dd HH:mm'),
                    createdBy: it.createdBy.username,
                    userId: it.createdBy.id,
                    comments: it.previewComments.comments.collect {
                        [
                            message: it.message,
                            createdBy: it.user.username,
                            userId: it.user.id,
                            picOriginal: it.user.picOriginal,
                            picLarge: it.user.picLarge,
                            picSmall: it.user.picSmall,
                            lastUpdated: it.lastUpdated.format('yyyy-MM-dd HH:mm')
                        ]
                    }
                ]
            }
            ret.status = 1
        }
        
        render ret as JSON
    }

    def topPost() {
        def ret = [:]
        def since
        def until
        ret.status = 0
        if(!params.max) {
            params.max = 10
        }
        if(params.since) {
            since = new Date().parse("yyyy-MM-dd HH:mm", params.since)
        }
        if(params.until) {
            until = new Date().parse("yyyy-MM-dd HH:mm", params.until)
        }
        def page = pageService.getTopPost(params.user, params.slug, params.fromId, params.toId, since, until, params.max)
        if(page) {
            ret.posts = page.posts.collect{
                [
                    id: it.id,
                    class: it.class.name.tokenize('.')[-1],
                    message: it.message,
                    dateCreated: it.dateCreated.format('yyyy-MM-dd HH:mm'),
                    createdBy: it.createdBy.username,
                    userPicSmall: it.createdBy.picSmall,
                    userId: it.createdBy.id,
                    conversation: it.conversation,
                    canDelete: pageService.canDelete(it.createdBy.id, params.user, page.author.isOwner),
                    comments: it.previewComments.comments.collect {
                        [
                            message: it.message,
                            createdBy: it.user.username,
                            userId: it.user.id,
                            picOriginal: it.user.picOriginal,
                            picLarge: it.user.picLarge,
                            picSmall: it.user.picSmall,
                            lastUpdated: it.lastUpdated.format('yyyy-MM-dd HH:mm'),
                            canDelete: pageService.canDelete(it.user.id, params.user, page.author.isOwner)
                        ]
                    }
                ]
            }
            ret.status = 1
        }
        
        render ret as JSON
    }

    def recentPost() {
        def ret = [:]
        def since
        def until
        ret.status = 0

        if(params.since) {
            since = new Date().parse("yyyy-MM-dd HH:mm", params.since)
        }
        if(params.until) {
            until = new Date().parse("yyyy-MM-dd HH:mm", params.until)
        }

        def page = pageService.getRecentPost(params.user, params.slug, params.fromId, params.toId, since, until, params.max)
        if(page) {
            ret.posts = page.posts.collect{
                def post = [
                    id: it.id,
                    class: it.class.name.tokenize('.')[-1],
                    message: it.message,
                    picSmall: it.picSmall,
                    picOriginal: it.picOriginal,
                    dateCreated: it.dateCreated.format('yyyy-MM-dd HH:mm'),
                    createdBy: it.createdBy.username,
                    userPicSmall: it.createdBy.picSmall,
                    userPicOriginal: it.createdBy.picOriginal,
                    userId: it.createdBy.id,
                    conversation: it.conversation,
                    lastActived: it.lastActived.time,
                    canDelete: pageService.canDelete(it.createdBy.id, params.user, page.author.isOwner),
                    comments: it.previewComments.comments.collect {
                        [
                            id: it.id,
                            message: it.message,
                            createdBy: it.user.username,
                            userId: it.user.id,
                            picOriginal: it.user.picOriginal,
                            picLarge: it.user.picLarge,
                            picSmall: it.user.picSmall,
                            lastUpdated: it.lastUpdated.format('yyyy-MM-dd HH:mm'),
                            canDelete: pageService.canDelete(it.user.id, params.user, page.author.isOwner)
                        ]
                    }
                ]
                if (it instanceof Need || it instanceof Resource) {
                    post.item = it.item
                    post.quantity = it.quantity
                    post.expiredDate = it.expiredDate
                }

                post
            }
            ret.status = 1
            ret.isJoined = page.author.isJoined
            ret.isOwner = page.author.isOwner
        }
        render ret as JSON
    }

    def need() {
        def ret = [:]
        def results = pageService.getAllNeeds(params.slug)
        ret.status = 1
        ret.needs = results.needs.collect {
            [   
                id: it.id,
                message: it.message,
                dateCreated: it.dateCreated.format('yyyy-MM-dd HH:mm'),
                createdBy: it.createdBy.username,
                userPicSmall: it.createdBy.picSmall,
                userPicOriginal: it.createdBy.picOriginal,
                userId: it.createdBy.id,
                expiredDate: it.expiredDate.format('yyyy-MM-dd HH:mm'),
                quantity: it.quantity,
                itemId: it.item.id,
                item: it.item.name,
                conversation: it.conversation,
                lastActived: it.lastActived.time,
                comments: it.previewComments.comments.collect {
                    [
                        id: it.id,
                        message: it.message,
                        createdBy: it.user.username,
                        userId: it.user.id,
                        picOriginal: it.user.picOriginal,
                        picLarge: it.user.picLarge,
                        picSmall: it.user.picSmall,
                        lastUpdated: it.lastUpdated.format('yyyy-MM-dd HH:mm')
                    ]
                }
            ]
        }
        ret.totalNeeds = results.totalNeeds
        render ret as JSON
    }

    def limitNeed() {
        def ret = [:]
        def limit = params.limit ?: 5
        def results = pageService.getLimitNeeds(params.slug, limit)
        ret.needs = results.needs.collect {
            [
                id: it.id,
                message: it.message,
                dateCreated: it.dateCreated.format('yyyy-MM-dd HH:mm'),
                createdBy: it.createdBy.username,
                userPicSmall: it.createdBy.picSmall,
                userPicOriginal: it.createdBy.picOriginal,
                userId: it.createdBy.id,
                expiredDate: it.expiredDate.format('yyyy-MM-dd HH:mm'),
                quantity: it.quantity,
                itemId: it.item.id,
                item: it.item.name,
                conversation: it.conversation,
                lastActived: it.lastActived.time
            ]
        }
        ret.totalNeeds = results.totalNeeds
        ret.status = 1
        render ret as JSON
    }

    def about() {
        def result = pageService.getAbout(params.slug)
        def about = [about:result]
        render about as JSON
    }

    def joinUs() {
        def userId = springSecurityService?.principal?.id
        def slug = params.slug
        def ret

        if(!userId || !slug) {
            ret = [status: 0, message: "user id: ${userId} could not be join page: ${slug}"]
        } else {
            try {
                def result = pageService.joinPage(userId, slug)
                ret = [status: 1, message: "user id: ${userId} joined page: ${slug}", slug: slug, page:result.page, user:result.user]
            } catch (e) {
                ret = [status: 0, message: "user id: ${userId} could not be join page: ${slug}"]
            }            
        }
        render ret as JSON
    }

    def createPage() {
        def userId = params.userId
        def name = params.name
        def lat = params.lat
        def lng = params.lng
        def household = params.household
        def population = params.population
        def about = params.about
        def location = params.location
        def picOriginal = params.picOriginal
        def picSmall = params.picSmall
        def picLarge = params.picLarge
        def ret = [:]

        if(!userId || !name) {
            ret = [status:0, message: "Can not create page",
                    lat: lat, lng: lng, household: household, population: population,
                    about: about, location: location, picOriginal: picOriginal, picSmall: picSmall, picLarge: picLarge]
            render ret as JSON
        } else {
            def errors
            def result
            try {
                result = pageService.createPage(userId, name, lat, lng, location, household, population, about, picOriginal, picSmall, picLarge)
            }
            catch (e) {
                errors = e
            }

            if (errors || ! result) {
                ret = [
                    status: 0,
                    message: "Can not save page"
                ]
            }
            else {
                ret = [
                    status: 1,
                    message: "Create page success",
                    userId: userId, 
                    name: result.name,
                    lat: result.lat,
                    lng: result.lng,
                    household: result.household, 
                    population: result.population,
                    about: result.about,
                    location: result.location, 
                    picOriginal: result.picOriginal,
                    picSmall: result.picSmall, 
                    picLarge: result.picLarge, 
                    slug: result.slug
                ]
            }
            render ret as JSON
        }
    }

    def leavePage() {
        def userId = springSecurityService?.principal?.id
        def slug = params.slug
        def ret
        if(!userId || !slug) {
            ret = [status: 0, message: "user id: ${userId} could not be left from page: ${slug}"]
        } else {
            try {
                pageService.leavePage(userId, slug)
                ret = [status: 1, message: "user id: ${userId} left from page: ${slug}", slug: slug, userId: userId]
            } catch (e) {
                ret = [status: 0, message: "user id: ${userId} could not be left from page: ${slug}", slug: slug]
            }            
        }
        render ret as JSON
    }

    def removeUserFromPage() {
        def userId = params.userId
        def slug = params.slug
        def ret
        if(!userId || !slug) {
            ret = [status: 0, message: "user id: ${userId} could not remove from page: ${slug}"]
            render ret as JSON
        } else {
            pageService.removeUserFromPage(userId, slug)
            ret = [status: 1, message: "user id: ${userId} removed from page: ${slug}"]
            render ret as JSON
        }
    }

    def setRelation() {
        def userId = params.userId
        def slug = params.slug
        def relation = params.relation
        def ret
        if(!userId || !slug) {
            ret = [status: 0, message: 'Can not change relation']
            render ret as JSON
        } else {
            pageService.setRelation(userId, slug, relation)
            ret = [status: 1, message: "user id: ${userId} had change relation to ${relation}"]
            render ret as JSON
        }
    }

    def postMessage() {
        def ret = [:]
        def userId = springSecurityService?.principal?.id
        def slug = params.slug
        def message = params.message
        def picOriginal = params.picOriginal
        def picSmall = params.picSmall

        def result = pageService.createMessagePost(userId, slug, message, picOriginal, picSmall)
        if(result) {
            ret = [post: [id :result.post.id, message: result.post.message,
            createdBy: result.post.createdBy, dateCreated: result.post.dateCreated, 
            lastActived: result.post.lastActived, picOriginal: result.post.picOriginal, picSmall: result.post.picSmall], user: result.user.username, 
            page: result.page.name, slug: result.page.slug]
            ret.status = 1
            ret.message = "user: ${result.user.username} posted message in page: ${result.page.name}"
        } else {
            ret.status = 0
            ret.message = "Error, Can't post this message"
        }
        
        render ret as JSON
    }

    def postNeed() {
        def ret = [:]
        def slug = params.slug
        def itemId = params.itemId
        def quantity = params.quantity
        def message = params.message
        def userId
        if(params.userId) {
            userId = params.long('userId')
        } else {
            userId = springSecurityService?.principal?.id
        }
        
        def result = pageService.createNeed(userId, slug, itemId, quantity, message)
        if(result.status == 1) {
            ret = [post: [id :result.post.id, message: result.post.message,
            item: [id: result.post.item.id, name: result.post.item.name], quantity: result.post.quantity,
            createdBy: result.post.createdBy, lastActived: result.post.lastActived], 
            user: result.user.username, page: result.page.name, slug: result.page.slug]
        }
        ret.status = result.status
        ret.message = "user: ${result.user.username} posted request ${result.post.item.name}, quantity: ${result.post.quantity} in page: ${result.page.name}"
        render ret as JSON
    }

    def postComment(){
        def userId = springSecurityService?.principal?.id
        def postId = params.postId
        def message = params.message
        def ret = [:]
        ret.status = 0

        if(userId && postId && message) {
            def result = pageService.postComment(userId, postId, message)
            ret = [status: 1, message: "user ${result.user.username} post comment ${result.comment.message} on post ${result.post.message}",
            userId: result.user.id, user: result.user.username, postId: result.post.id, 
            post: result.post.message, commentId: result.comment.id, comment: result.comment.message,
            picSmall: result.user.picSmall, lastUpdated: result.comment.lastUpdated]
        }
        render ret as JSON
    }    

    def updatePage() {
        def slug = params.slug
        def result = pageService.updatePage(slug, params)
        render result as JSON
    }

    def disablePage() {
        def slug = params.slug
        def result = pageService.disablePage(slug)
        def ret = [:]
        if(result.status==1) {
            ret = [status: result.status, page:[slug:result.page.slug, name:result.page.name, 
                    lat: result.page.lat, lng: result.page.lng, status:result.page.status.toString()]]
        } else {
            ret = [status: result.status, message: result.message]
        }
        render ret as JSON
    }

    def enablePage() {
        def slug = params.slug
        def result = pageService.enablePage(slug)
        def ret = [:] 
        if(result.status==1) {
            ret = [status: result.status, page:[slug:result.page.slug, name:result.page.name, 
                    lat: result.page.lat, lng: result.page.lng, status:result.page.status.toString()]]
        } else {
            ret = [status: result.status, message: result.message]
        }
        render ret as JSON
    }

    def searchPage() {
        def user = params.user
        def maxNeed = grailsApplication.config.infoaid.api.need.max 
        def ret = [:]
        ret.status = 0
        def word = params.word
        def offset = params.offset ? params.offset : 0
        def pages = pageService.searchPage(word, offset)
        if(pages) {
            ret.pages = pages.collect{
                [
                    id: it.id,
                    name: it.name,
                    lat: it.lat,
                    lng: it.lng,
                    household: it.household,
                    population: it.population,
                    about: it.about,
                    picSmall: it.picSmall,
                    slug: it.slug,
                    isJoined: user instanceof String? false:pageService.isJoined(user.id, it.slug).isJoined,
                    isOwner: user instanceof String? false:pageService.isOwner(user.id, it.slug).isOwner,

                    needs: pageService.getLimitNeeds(it.slug, maxNeed).needs.collect {
                       [
                            id: it.id,
                            item: it.item.name,
                            quantity: it.quantity
                       ]
                    }
                ]
            }
            ret.status = 1
            ret.totalResults = pages.size()
        }
        
        render ret as JSON
    }

    def disableComment() {
        def commentId = params.commentId
        def userId
        if(params.userId) {
            userId = params.long('userId')
        } else {
            userId = springSecurityService?.principal?.id
        } 
        //def userId = springSecurityService?.principal?.id
        def ret = pageService.disableComment(userId, commentId)
        render ret as JSON
    }

    def disablePost() {
        def postId = params.postId
        def userId
        if(params.userId) {
            userId = params.long('userId')
        } else {
            userId = springSecurityService?.principal?.id
        } 
        //def userId = springSecurityService?.principal?.id
        def ret = pageService.disablePost(userId, postId)
        render ret as JSON
    }

    def isOwner() {
        def slug = params.slug
        def user = params.user
        def result
        if(user instanceof String) {
            result = [isOwner: false]
        } else {
            result = pageService.isOwner(user.id, slug)
        }
        
        render result as JSON
    }

    def isJoined() {
        def slug = params.slug
        def user = params.user
        def result
        if(user instanceof String) {
            result = [isJoined: false]
        } else {
            result = pageService.isJoined(user.id, slug)
        }
        
        render result as JSON
    }

    def postResource() {
        def userId
        params.userId = springSecurityService?.principal?.id
        def result = pageService.createResource(params)
        render result as JSON
    }

    def getResource() {
        if(params.since) {
            params.since = new Date().parse("yyyy-MM-dd HH:mm", params.since)
        }
        if(params.until) {
            params.until = new Date().parse("yyyy-MM-dd HH:mm", params.until)
        }
        
        def result = pageService.getResource(params)
        render result as JSON
    }

    def itemHistory() {        
        def ret = [:]
        if(params.since) {
            params.since = new Date().parse("yyyy-MM-dd HH:mm", params.since)
        }
        if(params.until) {
            params.until = new Date().parse("yyyy-MM-dd HH:mm", params.until)
        }
        if(params.limit) {
            params.limit = params.int('limit')
        }
        if(params.itemId) {
            params.itemId = params.long('itemId')
        }
        
        def result = itemService.getItemHistory(params.user, params.slug, params.itemId, params.fromId, params.toId, params.since, params.until, params.limit)
        ret.status = 1
        ret.posts = result.itemHistory.collect {
            def post = [
                    id: it.id,
                    class: it.class.name.tokenize('.')[-1],
                    name: it.item.name,
                    message: it.message,
                    picSmall: it.picSmall,
                    picOriginal: it.picOriginal,
                    dateCreated: it.dateCreated.format('yyyy-MM-dd HH:mm'),
                    createdBy: it.createdBy.username,
                    userPicSmall: it.createdBy.picSmall,
                    userPicOriginal: it.createdBy.picOriginal,
                    userId: it.createdBy.id,
                    conversation: it.conversation,
                    lastActived: it.lastActived.time,
                    item: it.item,
                    quantity: it.quantity,
                    expiredDate: it.expiredDate,
                    canDelete: pageService.canDelete(it.createdBy.id, params.user, result.authority.isOwner),
                    comments: it.previewComments.comments.collect {
                        [
                            id: it.id,
                            message: it.message,
                            createdBy: it.user.username,
                            userId: it.user.id,
                            picOriginal: it.user.picOriginal,
                            picLarge: it.user.picLarge,
                            picSmall: it.user.picSmall,
                            lastUpdated: it.lastUpdated.format('yyyy-MM-dd HH:mm'),
                            canDelete: pageService.canDelete(it.user.id, params.user, result.authority.isOwner)
                        ]
                    }
                ]
        }

        ret.total = result.total
        ret.isJoined = result.authority.isJoined
        ret.isOwner = result.authority.isOwner

        render ret as JSON
    }
}