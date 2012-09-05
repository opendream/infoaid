package opendream.infoaid.service


import opendream.infoaid.domain.Role
import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(RoleService)
@Mock([Role])
class RoleServiceTests {

    @Before
    void setup() {
        def role1 = new Role(authority: 'role1Authority').save(flush: true)
        def role2 = new Role(authority: 'role2Authority').save(flush: true)
    }

    void testList() {
        def roleList = service.list()
        assert roleList.size() == 2
        assert roleList.first() == Role.get(1)
    }

    void testCreateRole() {
    	assert Role.count() == 2

    	service.createRole('superUser')

    	assert Role.count() == 3
    	assert Role.list().last().authority == 'superUser'
    }

    void testCreateRoleFail() {
    	assert Role.count() == 2

    	shouldFail(RuntimeException) {
            service.createRole('')
        }
    }
    
    void testShow() {
    	def role = service.show(1)
    	assert role == Role.get(1)
    }

    void testUpdate() {
    	def roleParams = [id: 1,authority: 'nut', status: Role.Status.INACTIVE]
        service.update(roleParams)

        def role1 = Role.get(1)

        assert role1.status == Role.Status.INACTIVE
        assert role1.authority == 'nut'
    }

    void testUpdateFail() {
    	def roleParams = [id: 1, authority: '', status: Role.Status.INACTIVE]
    	shouldFail(RuntimeException) {
        	service.update(roleParams)
        }

        def roleParams2 = [id: 1, authority: 'abc', version: -1]

        shouldFail(RuntimeException) {
        	service.update(roleParams2)
        }
    }

    void testDisable() {
    	def roleId = 1
    	service.disable(roleId)

    	def role = Role.get(roleId)
    	assert role.status == Role.Status.INACTIVE
    }

    void testEnable() {
    	def roleId = 1
    	service.enable(roleId)

    	def role = Role.get(roleId)
    	assert role.status == Role.Status.ACTIVE
    }
}
