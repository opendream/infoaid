package opendream.infoaid.service

import opendream.infoaid.domain.User

class UserService {
    def springSecurityService

    def create(userparams) {
        def user = new User()
        user.properties['username', 'password', 'firstname', 'lastname', 'email', 'telNo'] = userparams
        if(!user.save()) {
            log.error user.errors
            throw RuntimeException("${user.errors}")
        }
        user
    }

    def getBasicInfo(userId) {
        def user = User.get(userId)
        [username:user.username, firstname:user.firstname, lastname:user.lastname, email:user.email, telNo:user.telNo]
    }

    def updateBasicInfo(updateparmas) {
        def user = User.get(updateparmas.id)
        user.properties['username', 'firstname', 'lastname', 'email', 'telNo'] = updateparmas
        if(!user.save()) {
            log.error user.errors
            throw RuntimeException("${user.errors}")
        }
        [username:user.username, firstname:user.firstname, lastname:user.lastname, email:user.email, telNo:user.telNo]
    }

    def updatePassword(updateparmas) {
        if(updateparmas.newPassword != updateparmas.comfirmedPassword) {
            log.error "password confirmation miss match"
            //throw RuntimeException("password confirmation miss match")
            return [message: "password confirmation miss match"]
        }

        def user = User.get(updateparmas.id)

        if(user.password != springSecurityService.encodePassword(updateparmas.oldpassword)) {
            log.error "wrong password"
            //throw RuntimeException("wrong password")
            return [message: "wrong password"]
        }
        user.password = springSecurityService.encodePassword(updateparmas.newPassword)
        
        if(!user.save()) {
            log.error user.errors
            throw RuntimeException("${user.errors}")
        }
        return [message: "password is updated"]
    }

}
