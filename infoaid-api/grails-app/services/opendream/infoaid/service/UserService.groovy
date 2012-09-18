package opendream.infoaid.service

import opendream.infoaid.domain.User

class UserService {
    def springSecurityService

    def create(userparams) {
        def user = new User()
        user.properties['username', 'password', 'firstname', 'lastname', 'email', 'telNo'] = userparams
        if(!user.save()) {
            log.error user.errors
            throw new RuntimeException("${user.errors}")
        }
        user
    }

    def getBasicInfo(userId) {
        def user = User.get(userId)
        [id:user.id, username:user.username, firstname:user.firstname, lastname:user.lastname, email:user.email, telNo:user.telNo, 
        picOriginal: user.picOriginal, picSmall: user.picSmall, picLarge: user.picLarge]
    }

    def updateBasicInfo(updateparams) {
        def user = User.get(updateparams.id)
        user.properties['username', 'firstname', 'lastname', 'email', 'telNo', 'picOriginal'] = updateparams
        if(!user.save()) {
            log.error user.errors
            throw new RuntimeException("${user.errors}")
        }
        [username:user.username, firstname:user.firstname, lastname:user.lastname, email:user.email, telNo:user.telNo, picOriginal: user.picOriginal]
    }

    def updatePassword(updateparams) {
        if(updateparams.newPassword != updateparams.comfirmedPassword) {
            log.error "password confirmation miss match"
            //throw RuntimeException("password confirmation miss match")
            return [message: "password confirmation miss match"]
        }

        def user = User.get(updateparams.id)

        if(user.password != springSecurityService.encodePassword(updateparams.oldPassword)) {
            log.error "wrong password"
            //throw RuntimeException("wrong password")
            return [message: "wrong password"]
        }
        user.password = updateparams.newPassword
        
        if(!user.save(flush: true)) {
            log.error user.errors
            throw new RuntimeException("${user.errors}")
        }
        return [message: "password is updated"]
    }

}
