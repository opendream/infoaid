package opendream.infoaid.service

import opendream.infoaid.domain.User

class UserService {

    def create(userparams) {
        def user = new User()
        user.properties['username', 'password', 'firstname', 'lastname', 'email', 'telNo', 'dateCreated', 'lastUpdated'] = userparams
        if(!user.save()) {
            log.error user.errors
            throw RuntimeException("${user.errors}")
        }
        user
    }

    def getBasicInfo(userId) {
        def user = User.get(userId)
        [username:user.username, firstname:user.firstname, lastname:user.lastname, email:user.email]
    }

    def updateBasicInfo(userparams) {
        def user = User.get(userparams.id)
        user.properties['username', 'firstname', 'lastname', 'email', 'telNo'] = userparams
        if(!user.save()) {
            log.error user.errors
            throw RuntimeException("${user.errors}")
        }
        user
    }

}
