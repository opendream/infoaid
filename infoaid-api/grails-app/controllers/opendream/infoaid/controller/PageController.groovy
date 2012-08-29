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
        def ret = [:]
        def results = pageService.getPosts(params.slug, params.offset, params.max)
        ret.posts = results.posts.collect {
            [
                message: it.message,
                dateCreated: it.dateCreated.format('yyyy-MM-dd HH:mm'),
                comment: it.previewComments.message
            ]
        }
        ret.totalPosts = results.totalPosts
        render ret as JSON
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

    def summaryInfo() {
        def ret = [:]
        def pages = pageService.getSummaryInfo()
        ret.pages = pages.collect {
            [
                name: it.name,
                lat: it.lat,
                lng: it.lng,
                needs: pageService.getLimitNeeds(it.slug, 5).needs.collect {
                    [
                        message: it.message,
                        quantity: it.quantity
                    ]
                }

            ]
        }
        ret.totalPages = pages.size()
        
        render ret as JSON
    }
}