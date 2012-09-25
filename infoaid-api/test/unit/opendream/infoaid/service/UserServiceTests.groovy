package opendream.infoaid.service

import opendream.infoaid.domain.User

import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(UserService)
@Mock([User])
class UserServiceTests {
    def user

    @Before
    void setup() {
        User.metaClass.encodePassword = { -> delegate.password = delegate.password+'password'}
        User.metaClass.isDirty = {password -> true}

        user = new User(username: 'admin', password: 'password', 
            firstname: 'thawatchai', lastname: 'jong', picOriginal: 'picOri').save(flush:true)

        service.springSecurityService = [encodePassword: {pwd -> pwd?pwd+'password':''}]
    }

    void testCreate() {
        def userparams = [username: "nut", password: "nuttttt", firstname: 'firstname', 
        lastname: 'lastname']
        service.create(userparams)
        assert 2 == User.count()
        assert 'nut' == User.findByUsername('nut').username
    }

    void testCreateFail() {
        def userparams = [username: "", password: "nutttttt", firstname: 'firstname', 
        lastname: 'lastname', dateCreated: new Date(), lastUpdated: new Date()]
        shouldFail(RuntimeException) {
            service.create(userparams)
        }
    }

    void testGetBasicInfo() {
        def result = service.getBasicInfo(user.id)

        assert 'admin' == result.username
        assert 'thawatchai' == result.firstname
        assert 'jong' == result.lastname
        assert 'picOri' == result.picOriginal
        assert null == result.email
    }

    void testUpdateBasicInfo() {
        def updateparams = [userId:user.id, username: 'admin', firstname: 'thawatchai', 
        lastname: 'jong', email:'boyone@opendream.co.th', telNo:'12345678', picOriginal: 'picOri2']
        
        def result = service.updateBasicInfo(updateparams)
        assert 'admin' == result.username
        assert 'thawatchai' == result.firstname
        assert 'jong' == result.lastname
        assert 'boyone@opendream.co.th' == result.email
        assert 'picOri2' == result.picOriginal
        assert '12345678' == result.telNo      
    }

    void testUpdateBasicInfoFail() {
        def updateparams = [userId:user.id, username: '', 
            firstname: 'thawatchai', lastname: 'jong', email:'boyone@opendream.co.th']
        shouldFail(RuntimeException) {
            service.updateBasicInfo(updateparams)
        }
    }

    void testUpdatePassword() {   
        def updateparams = [userId:user.id, oldPassword: 'password', 
            newPassword: 'new-password', confirmedPassword: 'new-password']
        def result = service.updatePassword(updateparams)        
        assert "password is updated" == result.message

        def updateuser = User.get(user.id)
        assert "new-passwordpassword" == updateuser.password
    }

    void testUpdatePasswordWithWrongOldPassword() {
        def updateparams = [userId:user.id, oldPassword: 'passwordx', 
            newPassword: 'new-password', confirmedPassword: 'new-password']
        def result = service.updatePassword(updateparams)        
        assert "wrong password" == result.message
    }

    void testUpdatePasswordWithWrongNewPassword() {
        def updateparams = [userId:user.id, oldPassword: 'password', 
            newPassword: 'new-password', confirmedPassword: 'new-passwordx']
        def result = service.updatePassword(updateparams)        
        assert "password confirmation mismatch" == result.message
    }

    void testUpdatePasswordFail() {
        def updateparams = [userId:10000000, oldPassword: 'password', 
            newPassword: 'sssssss', confirmedPassword: 'sssssss']  
        shouldFail(RuntimeException) {
            service.updatePassword(updateparams)
        }
    }
}
