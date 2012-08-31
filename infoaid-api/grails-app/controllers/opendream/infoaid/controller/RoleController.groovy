package opendream.infoaid.controller

import opendream.infoaid.domain.Role
import grails.converters.JSON

class RoleController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def roles = Role.list(params)
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
        def role = new Role(params)
        if (!role.save()) {
            def errorMessage = [message: "Can't save this role"]
            log.error(role.errors)
            render errorMessage as JSON
            return
        }
    }

    def show(Long id) {
        def role = Role.get(id)
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

    def update(Long id, Long version) {        
        def roleInstance = Role.get(id)
        if (!roleInstance) {
            return
        }

        if (version != null) {
            if (roleInstance.version > version) {
                roleInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'role.label', default: 'Role')] as Object[],
                          "Another user has updated this Role while you were editing")
                return
            }
        }

        roleInstance.properties = params

        if (!roleInstance.save(flush: true)) {
            def errorMessage = "Can't update this role"
            render errorMessage as JSON
            return
        }
    }
    
    def disableRole(long id) {
        def role = Role.get(id)
        if(!role) {
            def errorMessage = "Role not found"
            render errorMessage as JSON
            return
        }

        role.status = Role.Status.INACTIVE
        if(!role.save()) {
            def errorMessage = "Can't disable this role"
            log.error(role.errors)
            render errorMessage as JSON
            return
        }
    }
    
}
