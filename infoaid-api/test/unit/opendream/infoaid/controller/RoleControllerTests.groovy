package opendream.infoaid.controller
import opendream.infoaid.domain.Role

import grails.test.mixin.*
import org.junit.*


@TestFor(RoleController)
@Mock(Role)
class RoleControllerTests {

    @Before
    void setup() {
        def role1 = new Role(authority: 'role1Authority').save(flush: true)
        def role2 = new Role(authority: 'role2Authority').save(flush: true)
    }

    void testList() {

        controller.list()
        assert response.json['totalRoles'] == 2
        assert response.json['roles'][0].authority == 'role1Authority'
    }

    void testCreateRole() {
        assert Role.count() == 2

        params.authority = 'role3Authority'
        controller.createRole()
        assert Role.count() == 3
    }

    void testShow() {
        def role = Role.get(1)

        params.id = role.id

        controller.show()
        assert 'role1Authority' == response.json['authority']

    }

    void testUpdate() {
        def role = Role.get(1)

        params.id = role.id
        params.version = role.version
        params.authority = 'newRole1Authority'

        controller.update()
        role = Role.findById(role.id)
        assert 'newRole1Authority' == role.authority

        params.id = role.id
        params.version = -1
        params.authority = 'newNewRole1Authority'
        controller.update()

        role = Role.findById(role.id)
        assert 'newRole1Authority' == role.authority
    }
    
    void testDisableRole() {
        def role = Role.get(1)

        params.id = role.id
        controller.disableRole()

        role = Role.findById(role.id)
        assert role.status == Role.Status.INACTIVE
    }
    
}
