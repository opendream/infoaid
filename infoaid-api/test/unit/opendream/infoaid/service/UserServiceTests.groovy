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
    }

    void testCreate() {
        def user = [username: "nut", password: "nut", firstname: 'firstname', 
        lastname: 'lastname', dateCreated: new Date(), lastUpdated: new Date()]
        service.create(user)
        assert 1 == User.count()
        assert 'nut' == User.findByUsername('nut').username
    }

    void testCreateFail() {
        def user = [username: "", password: "nut", firstname: 'firstname', 
        lastname: 'lastname', dateCreated: new Date(), lastUpdated: new Date()]
        shouldFail(RuntimeException) {
            service.create(user)
        }
    }

}
