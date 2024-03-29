package opendream.infoaid.service

import opendream.infoaid.domain.User
import opendream.infoaid.domain.Role
import opendream.infoaid.domain.UserRole
import opendream.infoaid.domain.Expertise
import opendream.infoaid.domain.Cause
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import java.net.URLDecoder

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
            def expertises = user.expertises.collect {
                [
                    id: it.id,
                    name: it.name,
                    description: it.description
                ]
            }

            def causes = user.causes.collect {
                [
                    id: it.id,
                    name: it.name,
                    description: it.description
                ]
            }

            [
                status: 1, id:user.id, username:user.username,
                firstname:user.firstname, lastname:user.lastname,
                email:user.email, telNo:user.telNo, picOriginal:user.picOriginal,
                picLarge:user.picLarge, picSmall:user.picSmall,
                expertises: expertises, causes: causes
            ]
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

    def updateExpertises(id, expertises) {
        def user = User.get(id),
            updatedExpertises = [],
            currentExpertises = []

        expertises.each {
            updatedExpertises.push(it)
        }
        user.expertises.each {
            currentExpertises.push(it.name)
        }

        // NOT WORK : Cannot remove expertise
        // def newExpertises = updatedExpertises - currentExpertises
        // def removedExpertises = currentExpertises - updatedExpertises
        // 
        // revokeExpertise(user.id, removedExpertises)
        // newExpertises.each {
        //    assignExpertise(user.id, it)
        // }

        user.expertises = [] as SortedSet
        user.expertises = getExpertise(updatedExpertises) as SortedSet
        user.save()
    }

    def updateCauses(id, causes) {
        def user = User.get(id),
            updatedCauses = [],
            currentCauses = []

        causes.each {
            updatedCauses.push(it)
        }
        user.causes.each {
            currentCauses.push(it.name)
        }

        user.causes = [] as SortedSet
        user.causes = getCause(updatedCauses) as SortedSet
        user.save()
    }

    def availableExpertises() {
        return Expertise.list()
    }

    def availableCauses() {
        return Cause.list()
    }

    def getCause(cause, createIfNotExists = false) {
        if (cause instanceof Cause || cause[0] instanceof Cause) {
            return cause
        }

        if (cause instanceof String) {
            cause = [cause]
        }

        cause.collect {
            if (it instanceof String) {
                it = Cause.findByName(URLDecoder.decode(it))
            }

            it
        }.findAll { it }
    }

    def getExpertise(expertise, createIfNotExists = false) {
        if (expertise instanceof Expertise || expertise[0] instanceof Expertise) {
            return expertise
        }

        if (expertise instanceof String) {
            expertise = [expertise]
        }

        expertise.collect {
            if (it instanceof String) {
                it = Expertise.findByName(URLDecoder.decode(it))
            }

            it
        }.findAll { it }
    }

    def assignExpertise(id, expertise) {
        def user = User.get(id)
        expertise = getExpertise(expertise)
        expertise.addToUsers(user).save()
    }

    def revokeExpertise(id, expertise) {
        def user = User.get(id)

        expertise = getExpertise(expertise)
        user.expertises = user.expertises - expertise
        user.save(failOnError: true)
    }

}
