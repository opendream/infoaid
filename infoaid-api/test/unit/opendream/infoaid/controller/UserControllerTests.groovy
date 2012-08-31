package opendream.infoaid.controller



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(UserController)
class UserControllerTests {

    def user

    @Before
    void setup() {
        User.metaClass.encodePassword = { -> delegate.password = delegate.password+'password'}
        User.metaClass.isDirty = {password -> true}

        user = new User(username: 'admin', password: 'password', 
            firstname: 'thawatchai', lastname: 'jong', dateCreated: new Date(), 
            lastUpdated: new Date()).save(flush:true)

        service.springSecurityService = [encodePassword: {pwd -> pwd?pwd+'password':''}]
    }

    void testCreate() {
        def params = [username: "nut", password: "nut", firstname: 'firstname', 
        lastname: 'lastname', dateCreated: new Date(), lastUpdated: new Date()]
        controller.create()
        assert 2 == User.count()
        assert 'nut' == User.findByUsername('nut').username
        assert 'nut' == response.json.username
    }
}
