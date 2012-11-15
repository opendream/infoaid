package opendream.infoaid.controller

import opendream.infoaid.domain.User
import opendream.infoaid.domain.Expertise
import opendream.infoaid.domain.Cause

import grails.converters.JSON

class UserController {
    def userService
    def springSecurityService

    def create() {
        def expertiseTmp = params.expertises,
            causeTmp = params.causes

        try { 
            def expertises = expertiseTmp.split(/\+/).collect {
                Expertise.get(it)
            } as SortedSet
            params.expertises = []

            def causes = causeTmp.split(/\+/).collect {
                Cause.get(it)
            } as SortedSet
            params.causes = []

            def user = userService.create(params)
            user.expertises = expertises
            user.causes = causes
            user.save()
            render user as JSON
        } catch (e) {
            log.error e
            params.expertises = expertiseTmp
            params.causes = causeTmp
            def resp = [message: 'can not create new user', user: params]
            render resp as JSON
        }
    }

    def showBasicInfo() {
        def result = userService.getBasicInfo(params.userId)
        render result as JSON
    }

    def updateBasicInfo() {
        def result
        try {
            result = userService.updateBasicInfo(params)
            render result as JSON
        } catch (e) {
            log.error e
            result = [status: 0, message: 'can not update user info', user: params]
            render result as JSON
        }
    }

    def updatePassword() {
        def  result
        try {
            result = userService.updatePassword(params)
            render result as JSON
        } catch (e) {
            result = [status: 0, message: 'can not update password', user: params]
            render result as JSON
        }
    }

    def availableCauses() {
        def result
        try {
            result = [
                status: 1,
                causes: userService.availableCauses().collect {
                    [
                        id: it.id,
                        name: it.name,
                        description: it.description,
                    ]
                },
            ]
        }
        catch (e) {
            println "Error: ${e}"
            result = [status: 0]
        }

        render result as JSON;
    }

    def availableExpertises() {
        def result
        try {
            result = [
                status: 1,
                expertises: userService.availableExpertises().collect {
                    [
                        id: it.id,
                        name: it.name,
                        description: it.description,
                    ]
                },
            ]
        }
        catch (e) {
            println "Error: ${e}"
            result = [status: 0]
        }

        render result as JSON;
    }

    def updateExpertises() {
        def result
        def expertises = JSON.parse(request.reader.text).expertises

        try {
            userService.updateExpertises(params.userId, expertises)
            result = [status: 1]
        }
        catch (e) {
            println "Fail to update expertise on user: ${params.userId}, Error is ${e}"
            result = [status: 0]
        }

        render result as JSON
    }

    def updateCauses() {
        def result
        def causes = JSON.parse(request.reader.text).causes

        try {
            userService.updateCauses(params.userId, causes)
            result = [status: 1]
        }
        catch (e) {
            println "Fail to update cause on user: ${params.userId}, Error is ${e}"
            result = [status: 0]
        }

        render result as JSON
    }

    def getPages() {
        def ret = [:]
        ret.status = 0
        def user = User.get(params.user.id)
        def pages = user.getPages()
        if(pages) {
            ret.totalPages = pages.size()
            ret.pages = pages.collect{
                [   
                    id: it.id,
                    name: it.name,
                    slug: it.slug
                ]
            }
            ret.status = 1
        }
        render ret as JSON
    }

    def authenticate() {
        def userId = springSecurityService.principal?.id
        def result = userService.getBasicInfo(userId)
        render result as JSON
    }
}
