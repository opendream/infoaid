package opendream.infoaid.service

import opendream.infoaid.domain.Role

class RoleService {

    def list(Integer max) {
    	def params = [:]
    	params.max = Math.min(max ?: 10, 100)
    	def role = Role.list(params)
    }

    def createRole(authority) {
    	def newRole = new Role(authority: authority)
    	if(!newRole.save()) {
    		log.error newRole.errors
            throw new RuntimeException("${newRole.errors}")
    	}
    	newRole
    }

    def show(roleId) {
    	def role = Role.get(roleId)
    }

    def update(roleParams) {
    	def role = Role.get(roleParams.id)

    	if(!role) {
    		log.error role.errors
    		throw new RuntimeException("${role.errors}")
    		return
    	}

    	if (roleParams.version != null) {
            if (role.version > roleParams.version) {
                log.error role.errors
	    		throw new RuntimeException("${role.errors}")
	    		return
            }
        }

        role.properties['authority', 'status'] = roleParams

    	if(!role.save()) {
    		log.error role.errors
    		throw new RuntimeException("${role.errors}")
    		return
    	}
    	[id: role.id, authority: role.authority]
    }

    def disable(roleId) {
    	def role = Role.get(roleId)

    	if(role) {
    		role.status = Role.Status.INACTIVE
    	}

    	if(!role.save()) {
    		log.error role.errors
    		throw new RuntimeException("${role.errors}")
    	}
    	[id: role.id, status: role.status]
    }

    def enable(roleId) {
    	def role = Role.get(roleId)

    	if(role) {
    		role.status = Role.Status.ACTIVE
    	}

    	if(!role.save()) {
    		log.error role.errors
    		throw new RuntimeException("${role.errors}")
    	}
    	[id: role.id, status: role.status]
    }
}
