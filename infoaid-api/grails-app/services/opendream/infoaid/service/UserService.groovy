package opendream.infoaid.service

import opendream.infoaid.domain.User
import opendream.infoaid.domain.Role
import opendream.infoaid.domain.UserRole
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class UserService {
    def springSecurityService

    def create(userparams) {
        def passLength = userparams['password'].size()

        if(passLength < 7 || passLength > 20) {
            log.error "password confirmation miss match"
            return [message: "Password must have 7 to 20 character"]
        }
        def user = new User()
        user.properties['username', 'password', 'firstname', 'lastname', 'email', 'telNo', 'accountExpired', 'accountLocked', 'passwordExpired'] = userparams

        user.picSmall = userparams['picSmall'] ?: null
        user.picOriginal = userparams['picOriginal'] ?: null
        user.picLarge = userparams['picLarge'] ?: null
        if(!user.validate()) {
            //log.error user.errors
            throw new RuntimeException("${user.errors}")
        }
        user.save(failOnError:true, flush:true)
        def roleUser = ConfigurationHolder.config.infoaid.api.user.role

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
        if(!updateparams['picOriginal'] || !updateparams['picLarge'] || !updateparams['picSmall']) {
            user.picSmall = '/media/profiles/profile_default_Small.png'
            user.picOriginal = '/media/profiles/profile_default_Original.png'
            user.picLarge = '/media/profiles/profile_default_large.png'
        }
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
