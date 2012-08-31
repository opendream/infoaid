package opendream.infoaid.controller

import opendream.infoaid.domain.User

import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(UserController)
@Mock([User])
class UserControllerTests {
    def user

    @Before
    void setup() {
        User.metaClass.encodePassword = { -> delegate.password = delegate.password+'password'}
        User.metaClass.isDirty = {password -> true}

        user = new User(username: 'admin', password: 'password', 
            firstname: 'thawatchai', lastname: 'jong', dateCreated: new Date(), 
            lastUpdated: new Date()).save(flush:true)
        
        controller.userService = [create: { mockparams -> def newuser = new User()
                                        newuser.properties = mockparams 
                                        newuser.save() } ]
    }

    void testCreate() {
        params.username = "nut"
        params.password = "nut"
        params.firstname = 'firstname'
        params.lastname = 'lastname'
        params.dateCreated = new Date()
        params.lastUpdated = new Date()
        controller.create()
        assert 2 == User.count()
        assert 'nut' == User.findByUsername('nut').username
        assert 'nut' == response.json.username
    }

    void testCreateFail() {
        controller.userService = [create: { mockparams -> throw RuntimeException("errors") } ]

        params.username = "nut"
        params.password = "nut"
        params.firstname = 'firstname'
        params.lastname = 'lastname'
        params.dateCreated = new Date()
        params.lastUpdated = new Date()
        controller.create()
        assert 1 == User.count()
        assert 'can not create new user' == response.json.message
        assert 'nut' == response.json.user.username
    }
}
