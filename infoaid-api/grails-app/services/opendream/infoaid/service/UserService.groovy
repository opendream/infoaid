package opendream.infoaid.service

import opendream.infoaid.domain.User

class UserService {

    def create(userparams) {
        def user = new User()
        user.properties['username', 'password', 'firstname', 'lastname', 'dateCreated', 'lastUpdated'] = userparams
        if(!user.save()) {
            log.error user.errors
            throw RuntimeException("${user.errors}")
        }
        user
    }
}
