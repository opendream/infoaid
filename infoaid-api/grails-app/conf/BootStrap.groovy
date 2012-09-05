import opendream.infoaid.domain.User

class BootStrap {

    def init = { servletContext ->
        development {
            if(!User.count()) {
                println "insert user"
                new User(username: 'admin', password: 'password', 
                firstname: 'thawatchai', lastname: 'jong', dateCreated: new Date(), 
                lastUpdated: new Date()).save(flush:true)
            }
        }
    }
    def destroy = {
    }
}
