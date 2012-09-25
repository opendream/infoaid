package opendream.infoaid.controller

import grails.converters.JSON
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class PageController {
    def pageService
    def springSecurityService

    def index() { }

    def info() {
        def ret = [:]
        ret.status = 0
        def info = pageService.getInfo(params.slug)
        if(info) {
            ret = [id: info.id, name: info.name, lat: info.lat, lng: info.lng, dateCreated: info.dateCreated.format('yyyy-MM-dd HH:mm'), 
            lastUpdated: info.lastUpdated.format('yyyy-MM-dd HH:mm'), picSmall: info.picSmall, household: info.household, 
            population: info.population, status: 1, slug: info.slug
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
        def offset = params.offset ? params.offset : 0
        def max = params.max ? params.max : ConfigurationHolder.config.infoaid.api.allmember.limited
        def results = pageService.getMembers(params.slug, offset, max)
        ret.status = 1
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
                relation: it.relation.toString()
            ]
        }
        ret.totalMembers = results.size()
        render ret as JSON
    }

    def topMember() {
        def ret = [:]
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
        def posts = pageService.getTopPost(params.slug, params.fromId, params.toId, since, until, params.max)
        if(posts) {
            ret.posts = posts.collect{
                [
                    id: it.id,
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

    def recentPost() {
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
        def posts = pageService.getRecentPost(params.slug, params.fromId, params.toId, since, until, params.max)
        if(posts) {
            ret.posts = posts.collect{
                [
                    id: it.id,
                    message: it.message,
                    dateCreated: it.dateCreated.format('yyyy-MM-dd HH:mm'),
                    createdBy: it.createdBy.username,
                    userId: it.createdBy.id,
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
            ret.status = 1
        }
        
        render ret as JSON
    }

    def need() {
        def ret = [:]
        def results = pageService.getAllNeeds(params.slug)
        ret.status = 1
        ret.needs = results.needs.collect {
            [
                message: it.message,
                dateCreated: it.dateCreated.format('yyyy-MM-dd HH:mm'),
                createdBy: it.createdBy.username,
                userId: it.createdBy.id,
                expiredDate: it.expiredDate.format('yyyy-MM-dd HH:mm'),
                quantity: it.quantity,
                item: it.item.name
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
                message: it.message
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
        def userId = params.userId
        def slug = params.slug
        def ret

        if(!userId || !slug) {
            ret = [status: 0, message: "user id: ${userId} could not be join page: ${slug}"]
        } else {
            try {
                pageService.joinPage(userId, slug)
                ret = [status: 1, message: "user id: ${userId} joined page: ${slug}"]
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
        def ret = [:]

        if(!userId || !name) {
            ret = [status:0, message: "user id: ${userId} could not create page: ${name}",
                    lat: lat, lng: lng, household: household, population: population,
                    about: about, location: location, picOriginal: picOriginal]
            render ret as JSON
        } else {
            def result = pageService.createPage(userId, name, lat, lng, location, household, population, about, picOriginal)
            ret = [status:1, message: "user id: ${userId} created page: ${name}", userId: userId, 
                    name: result.name, lat: result.lat, lng: result.lng, household: result.household, 
                    population: result.population, about: result.about, location: result.location, picOriginal: result.picOriginal, slug: result.slug]
            render ret as JSON
        }
    }

    def leavePage() {
        def userId = params.userId
        def slug = params.slug
        def ret
        if(!userId || !slug) {
            ret = [status: 0, message: "user id: ${userId} could not be left from page: ${slug}"]
        } else {
            try {
                pageService.leavePage(userId, slug)
                ret = [status: 1, message: "user id: ${userId} left from page: ${slug}"]
            } catch (e) {
                ret = [status: 0, message: "user id: ${userId} could not be left from page: ${slug}"]
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
        def ret
        def userId = params.userId
        def slug = params.slug
        def message = params.message

        def result = pageService.createMessagePost(userId, slug, message)
        ret = [post: [id :result.post.id, message: result.post.message, 
        createdBy: result.post.createdBy, dateCreated: result.post.dateCreated, 
        lastActived: result.post.lastActived], user: result.user.username, 
        page: result.page.name, slug: result.page.slug]
        ret.status = 1
        ret.message = "user: ${result.user.username} posted message in page: ${result.page.name}"
        render ret as JSON
    }

    def postNeed() {
        def ret
        def userId = params.userId
        def slug = params.slug
        def itemId = params.itemId
        def quantity = params.quantity
        def message = params.message

        def result = pageService.createNeed(userId, slug, itemId, quantity, message)
        ret = [post: [id :result.post.id, message: result.post.message,
        item: [id: result.post.item.id, name: result.post.item.name], quantity: result.post.quantity,
        createdBy: result.post.createdBy, lastActived: result.post.lastActived], 
        user: result.user.username, page: result.page.name, slug: result.page.slug]
        ret.status = 1
        ret.message = "user: ${result.user.username} posted request ${result.post.item.name}, quantity: ${result.post.quantity} in page: ${result.page.name}"
        render ret as JSON
    }

    def postComment(){
        def userId = params.userId
        def postId = params.postId
        def message = params.message
        def ret

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

        pageService.updatePage(slug, params)
    }

    def disablePage() {
        def slug = params.slug
        def page = pageService.disablePage(slug)
        def ret = [status:1, page:[slug:page.slug, name:page.name, 
                    lat: page.lat, lng: page.lng, status:page.status.toString()]]
        render ret as JSON
    }

    def enablePage() {
        def slug = params.slug
        def page = pageService.enablePage(slug)
        def ret = [status:1, page:[slug:page.slug, name:page.name, 
                    lat: page.lat, lng: page.lng, status:page.status.toString()]]
        render ret as JSON
    }

    def searchPage() {
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

                    needs: pageService.getLimitNeeds(it.slug, 4).needs.collect {
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
        def userId = params.userId
        def slug = params.slug

        def result = pageService.isOwner(userId, slug)
        render result as JSON
    }

    def isJoined() {
        def userId = params.userId
        def slug = params.slug

        def result = pageService.isJoined(userId, slug)
        render result as JSON
    }
}