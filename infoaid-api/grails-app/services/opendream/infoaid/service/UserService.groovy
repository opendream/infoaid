package opendream.infoaid.service

import opendream.infoaid.domain.User

class UserService {
    def springSecurityService

    def create(userparams) {
        def passLength = userparams['password'].size()

        if(passLength < 7 || passLength > 20) {
            log.error "password confirmation miss match"
            return [message: "Password must have 7 to 20 character"]
        }
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
        if(user) {
            [status:1, id:user.id, username:user.username, firstname:user.firstname, 
            lastname:user.lastname, email:user.email, telNo:user.telNo, 
            picOriginal:user.picOriginal, picLarge:user.picLarge, picSmall:user.picSmall]
        } else {
            [status:0, message:'user not found']
        }
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
        def passLength = updateparams['newPassword'].size()
        println updateparams
        if(passLength < 7 || passLength > 20) {
            log.error "password confirmation mismatch"
            return [message: "Password must have 7 to 20 character"]
        }
        println "${updateparams.newPassword} != ${updateparams.confirmedPassword}"
        if(updateparams.newPassword != updateparams.confirmedPassword) {
            log.error "password confirmation mismatch"
            //throw RuntimeException("password confirmation mismatch")
            return [message: "password confirmation mismatch"]
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
