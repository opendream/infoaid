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
        def ret = [:]
        def results = pageService.getPosts(params.slug, params.offset, params.max)
        ret.posts = results.posts.collect{
            [
                message: it.message,
                dateCreated: it.dateCreated.format('yyyy-MM-dd HH:mm'),
                comment: it.previewComments.message
            ]
        }
        ret.totalPosts = results.totalPosts
        render ret as JSON
    }

    def needs() {
        def needs = pageService.getNeeds()
    }

    def about() {
        def result = pageService.getAbout(params.slug)
        render result
    }
}