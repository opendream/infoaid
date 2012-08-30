package opendream.infoaid.controller

import opendream.infoaid.domain.User

import grails.converters.JSON

class HomeController {
    def homeService

    def recentPost() { 
        def user = User.get(params.userId)
        def posts = homeService.getFeedByRecentPost(user)
        render posts as JSON
    }

    def topPost() {
        def user = User.get(params.userId)
        def posts = homeService.getFeedByTopPost(user)
        render posts as JSON
    }
}
