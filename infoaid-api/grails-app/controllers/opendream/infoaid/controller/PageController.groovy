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
            ret = [id: map.id, name: map.name, lat: map.lat, lng: map.lng]
        }

        render ret as JSON
    }

    def member() {
        def results = pageService.getMembers(params.slug)
        def members
        members = results.collect{
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
        render members as JSON
    }

    def topMember() {
        def results = pageService.getTopMembers(params.slug)
        def topMembers = results.collect {
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
        render topMembers as JSON
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
        def posts = pageService.getTopPost(params.slug, params.offset)
        
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
        render ret as JSON
    }

    def about() {
        def result = pageService.getAbout(params.slug)
        render result
    }

    def joinUs() {
        def userId = params.userId
        if(!userId) {
            return
        } else {
            pageService.joinPage(userId, params.slug)
        }
    }
}