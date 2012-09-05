package opendream.infoaid.controller

import opendream.infoaid.domain.Role
import grails.converters.JSON

class RoleController {

    def roleService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list(Integer max) {
        def roles = roleService.list(max)
        def ret = [:]
        ret.roles = roles.collect {
            [
                authority: it.authority,
                status: it.status.toString()
            ]
        }
        ret.totalRoles = roles.size()
        render ret as JSON
    }

    def createRole() {
        def ret = [:]
        try{
            def newRole = roleService.createRole(params.authority)
            ret.authority = newRole.authority
            ret.status = newRole.status.toString()

            render ret as JSON
        } catch(e) {
            ret = [message: 'can not create new role', role: params]
            render ret as JSON
        }
        
    }

    def show(Long id) {
        def role = roleService.show(id)
        if (!role) {
            def errorMessage = [message: "Role not Found"]
            render errorMessage as JSON
            return
        }

        def ret = [:]
        ret.authority = role.authority
        ret.status = role.status.toString()

        render ret as JSON
    }

    def update() {
        def ret = [:]
        try {
            def role = roleService.update(params)
            ret.id = role.id
            ret.authority = role.authority
            render ret as JSON
        } catch (e) {
            ret = [message: 'can not edit this role', role: params]
            render ret as JSON
        }
    }
    
    def disableRole() {
        def ret = [:]
        try {
            def role = roleService.disable(params.id)
            ret.id = role.id
            ret.status = role.status.toString()
            render ret as JSON
        } catch (e) {
            ret = [message: 'can not disable this role', role: params]
            render ret as JSON
        }
    }

    def enableRole() {
        def ret = [:]
        try {
            def role = roleService.enable(params.id)
            ret.id = role.id
            ret.status = role.status.toString()
            render ret as JSON
        } catch (e) {
            ret = [message: 'can not enable this role', role: params]
            render ret as JSON
        }
    }
    
}
