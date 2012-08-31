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
    @Before
    void setup() {
        User.metaClass.encodePassword = { -> 'password'}
        User.metaClass.isDirty = {password -> false}

        new User(username: 'admin', password: 'password', 
            firstname: 'thawatchai', lastname: 'jong', dateCreated: new Date(), 
            lastUpdated: new Date()).save(flush:true)
    }

    void testCreate() {
        def user = [username: "nut", password: "nut", firstname: 'firstname', 
        lastname: 'lastname', dateCreated: new Date(), lastUpdated: new Date()]
        service.create(user)
        assert 2 == User.count()
        assert 'nut' == User.findByUsername('nut').username
    }

    void testCreateFail() {
        def user = [username: "", password: "nut", firstname: 'firstname', 
        lastname: 'lastname', dateCreated: new Date(), lastUpdated: new Date()]
        shouldFail(RuntimeException) {
            service.create(user)
        }
    }

    void testGetBasicInfo() {
        def user = User.findByUsername('admin')
        def result = service.getBasicInfo(user.id)

        assert 'admin' == result.username
        assert 'thawatchai' == result.firstname
        assert 'jong' == result.lastname
        assert null == result.email
    }

    void testUpdateBasicInfo() {
        def user = User.findByUsername('admin')
        def updateUser = [id:user.id, username: 'admin', firstname: 'thawatchai', 
        lastname: 'jong', email:'boyone@opendream.co.th', telNo:'12345678']
        
        def result = service.updateBasicInfo(updateUser)
        assert 'admin' == result.username
        assert 'thawatchai' == result.firstname
        assert 'jong' == result.lastname
        assert 'boyone@opendream.co.th' == result.email 
        assert '12345678' == result.telNo      
    }

    void testUpdateBasicInfoFail() {
        def user = User.findByUsername('admin')
        def updateUser = [id:user.id, username: '', 
            firstname: 'thawatchai', lastname: 'jong', email:'boyone@opendream.co.th']
        shouldFail(RuntimeException) {
            service.updateBasicInfo(updateUser)
        }
    }


}
