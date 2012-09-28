package opendream.infoaid.service

import opendream.infoaid.domain.User
import opendream.infoaid.domain.Role
import opendream.infoaid.domain.UserRole

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
        def roleUser = grailsApplication.config.infoaid.api.user.role
        def role = Role.findByAuthority(roleUser)
        if(!UserRole.create(user, role)) {
            throw new RuntimeException("can not assign role to user: ${user.id}")
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
        def user = User.get(updateparams.userId)
        user.properties['username', 'firstname', 'lastname', 'email', 'telNo', 'picOriginal', 'picLarge', 'picSmall'] = updateparams
        if(!user.save()) {
            log.error user.errors
            throw new RuntimeException("${user.errors}")
        }
        [status:1, username:user.username, firstname:user.firstname, lastname:user.lastname, email:user.email, 
        telNo:user.telNo, picOriginal: user.picOriginal, picLarge: user.picLarge, picSmall: user.picSmall]
    }

    def updatePassword(updateparams) {
        def passLength = updateparams['newPassword'].size()
        if(passLength < 7 || passLength > 20) {
            log.error "password confirmation mismatch"
            return [status: 0, message: "Password must have 7 to 20 character"]
        }
        if(updateparams.newPassword != updateparams.confirmedPassword) {
            log.error "password confirmation mismatch"
            //throw RuntimeException("password confirmation mismatch")
            return [status: 0, message: "password confirmation mismatch"]
        }

        def user = User.get(updateparams.userId)

        if(user.password != springSecurityService.encodePassword(updateparams.oldPassword)) {
            log.error "wrong password"
            //throw RuntimeException("wrong password")
            return [status: 0, message: "wrong password"]
        }
        user.password = updateparams.newPassword
        
        if(!user.save(flush: true)) {
            log.error user.errors
            throw new RuntimeException("${user.errors}")
        }
        return [message: "password is updated"]
    }

}
