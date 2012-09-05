package opendream.infoaid.controller

import grails.converters.JSON

class PageController {
    def pageService

    def index() { }

    def info() {
        def ret = [:]
        def info = pageService.getInfo(params.slug)
        if(info) {
            ret = [id: info.id, name: info.name, lat: info.lat, lng: info.lng, dateCreated: info.dateCreated.format('yyyy-MM-dd HH:mm'), 
            lastUpdated: info.lastUpdated.format('yyyy-MM-dd HH:mm')
            ]
        }
        
        render ret as JSON
        
    }

    def map() {
        def ret = [:]
        def map = pageService.getInfo(params.slug)
        if(map) {
            ret = [status:1, id: map.id, name: map.name, lat: map.lat, lng: map.lng]
        }

        render ret as JSON
    }

    def member() {
        def ret = [:]
        def results = pageService.getMembers(params.slug)
        ret.status = 1
        ret.members = results.collect{
            [
                id: it.user.id,
                username: it.user.username,
                firstname: it.user.firstname,
                lastname: it.user.lastname,
                email: it.user.email,
                telNo: it.user.telNo,
                relation: it.relation.toString()
            ]
        }
        ret.totalMembers = results.size()
        render ret as JSON
    }

    def topMember() {
        def ret = [:]
        def results = pageService.getTopMembers(params.slug)
        ret.topMembers = results.collect {
            [   
                id: it.user.id,
                username: it.user.username,
                firstname: it.user.firstname,
                lastname: it.user.lastname,
                email: it.user.email,
                telNo: it.user.telNo,
                relation: it.relation.toString()
            ]
        }
        ret.totalTopMembers = results.size()
        render ret as JSON
    }

    def status() {
        def posts = pageService.getPosts(params.slug, params.offset, params.max)
        
        posts = posts.collect{
            [
                message: it.message,
                dateCreated: it.dateCreated.format('yyyy-MM-dd HH:mm'),
                comments: it.previewComments.message
            ]
        }
        //ret.totalPosts = results.totalPosts
        render posts as JSON
    }

    def topPost() {
        def posts = pageService.getTopPost(params.slug)
        
        posts = posts.collect{
            [
                message: it.message,
                dateCreated: it.dateCreated.format('yyyy-MM-dd HH:mm'),
                comments: it.previewComments.message
            ]
        }
        //ret.totalPosts = results.totalPosts
        render posts as JSON
    }

    def recentPost() {
        def posts = pageService.getRecentPost(params.slug)
        
        posts = posts.collect{
            [
                message: it.message,
                dateCreated: it.dateCreated.format('yyyy-MM-dd HH:mm'),
                comments: it.previewComments.message
            ]
        }
        //ret.totalPosts = results.totalPosts
        render posts as JSON
    }

    def need() {
        def ret = [:]
        def results = pageService.getAllNeeds(params.slug)
        ret.status = 1
        ret.needs = results.needs.collect {
            [
                message: it.message,
                dateCreated: it.dateCreated.format('yyyy-MM-dd HH:mm'),
                createdBy: it.createdBy,
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
                message: it.message,
                dateCreated: it.dateCreated.format('yyyy-MM-dd HH:mm'),
                createdBy: it.createdBy,
                expiredDate: it.expiredDate.format('yyyy-MM-dd HH:mm'),
                quantity: it.quantity,
                item: it.item.name
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
        def ret
        def userId = params.userId
        if(!userId) {
            return
        } else {
            def pageuser = pageService.joinPage(userId, params.slug)
            ret = [user: pageuser.user.username, page: pageuser.page.name, 
                    pageSlug: pageuser.page.slug, relation: pageuser.relation.toString()]
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
        def ret = [:]

        if(!userId || !name) {
            ret = [status:0, message: "user id: ${userId} could not create page: ${name}",
                    lat: lat, lng: lng, household: household, population: population,
                    about: about, location: location]
            render ret as JSON
        } else {
            def result = pageService.createPage(userId, name, lat, lng, location, household, population, about)
            ret = [status:1, message: "user id: ${userId} created page: ${name}", userId: userId, 
                    name:result.name, lat: result.lat, lng: result.lng, household: result.household, 
                    population: result.population, about: result.about, location: result.location]
            render ret as JSON
        }
    }

    def leavePage() {
        def userId = params.userId
        def slug = params.slug
        def ret
        if(!userId || !slug) {
            ret = [status: 0, message: "user id: ${userId} could not be left from page: ${slug}"]
            render ret as JSON
        } else {
            pageService.leavePage(userId, slug)
            ret = [status: 1, message: "user id: ${userId} left from page: ${slug}"]
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
        createdBy: result.post.createdBy, lastActived: result.post.lastActived], 
        user: result.user.username, page: result.page.name, slug: result.page.slug]
        ret.status = 1
        ret.message = "user: ${result.user.username} posted message in page: ${result.page.name}"
        render ret as JSON
    }

    def postComment(){
        def userId = params.userId
        def postId = params.postId
        def message = params.message

        if(userId && postId && message) {
            pageService.postComment(userId, postId, message)
        }
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
}