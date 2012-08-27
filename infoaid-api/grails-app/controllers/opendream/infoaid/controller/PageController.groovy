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

    def status() {
        def ret = [:]
        def results = pageService.getPosts(params.slug, params.offset, params.max)
        ret.totalPosts = results.totalPosts
        //println results.posts

        ret.posts = results.posts.collect{
            [
                message: it.message,
                dateCreated: it.dateCreated
            ]
        }
        println ret
        render ret as JSON
    }

    def needs() {
        def needs = pageService.getNeeds()
    }
}
