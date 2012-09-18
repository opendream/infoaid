import opendream.infoaid.domain.User
import opendream.infoaid.domain.Role
import opendream.infoaid.domain.UserRole

class BootStrap {

    def init = { servletContext ->
        development {
            if(!User.count()) {
                println "insert user"
                def user = new User(username: 'admin', password: 'password', 
                firstname: 'thawatchai', lastname: 'jong', dateCreated: new Date(), 
                lastUpdated: new Date()).save(flush:true)
                def role = new Role(authority:'ROLE_ADMIN').save(flush:true)
                UserRole.create(user, role)
            }
        }
    }
    def destroy = {
    }
}
