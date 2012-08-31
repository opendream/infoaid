package opendream.infoaid.controller

import grails.converters.JSON

class UserController {
    def userService

    def create() {
        try { 
            def user = userService.create(params)
            render user as JSON
        } catch (e) {
            def resp = [message: 'can not create new user', user: params]
            render resp as JSON
        }
    }

    def showBasicInfo() {
        def result = userService.getBasicInfo(params.id)
        render result as JSON
    }

    def updateBasicInfo() {
        try {
            def result = userService.updateBasicInfo(params)
            render result as JSON
        } catch (e) {
            def resp = [message: 'can not update user info', user: params]
            render resp as JSON
        }
    }
}
