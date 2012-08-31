package opendream.infoaid.controller

import opendream.infoaid.domain.User
import opendream.infoaid.service.UserService

import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(UserController)
@Mock([User])
class UserControllerTests {
    def user
    def userService

    @Before
    void setup() {
        User.metaClass.encodePassword = { -> delegate.password = delegate.password+'password'}
        User.metaClass.isDirty = {password -> true}

        user = new User(username: 'admin', password: 'password', 
            firstname: 'thawatchai', lastname: 'jong', dateCreated: new Date(), 
            lastUpdated: new Date()).save(flush:true)
        
        userService = mockFor(UserService)
        //controller.userService = [create: { mockparams -> def newuser = new User()
                                        //newuser.properties = mockparams 
                                        //newuser.save() } ]
    }

    void testCreate() {
        userService.demand.create(1..1) { mockparams -> def newuser = new User(mockparams).save() }
        controller.userService = userService.createMock()

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
        controller.userService = [create: { mockparams -> throw RuntimeException("errors") }]

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

    void testShowBasicInfo() {        
        userService.demand.getBasicInfo(1..1) { userId -> [username:user.username, firstname:user.firstname, 
                                                            lastname:user.lastname, email:user.email] } 
        controller.userService = userService.createMock()

        params.userId = user.id
        controller.showBasicInfo()

        assert 1 == User.count()
        assert 'admin' == response.json.username
        assert 'jong' == response.json.lastname
    }
}
