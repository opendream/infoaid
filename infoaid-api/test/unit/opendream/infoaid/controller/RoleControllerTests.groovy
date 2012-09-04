package opendream.infoaid.controller
import opendream.infoaid.domain.Role
import opendream.infoaid.service.RoleService

import grails.test.mixin.*
import org.junit.*


@TestFor(RoleController)
@Mock([Role, RoleService])
class RoleControllerTests {
    def roleService

    @Before
    void setup() {
        roleService = mockFor(RoleService)

        def role1 = new Role(authority: 'role1Authority').save(flush: true)
        def role2 = new Role(authority: 'role2Authority').save(flush: true)
    }

    void testList() {
        roleService.demand.list(1..1) {max -> 
            def params = [:]
            params.max = Math.min(max ?: 10, 100)
            def role = Role.list(params)
        }
        controller.roleService = roleService.createMock()

        controller.list()
        assert response.json['totalRoles'] == 2
        assert response.json['roles'][0].authority == 'role1Authority'
    }

    void testCreateRole() {
        roleService.demand.createRole(1..2) {authority ->
            def newRole = new Role(authority: authority) 
            if(!newRole.save()) {
                log.error newRole.errors
                throw new RuntimeException("${newRole.errors}")
            }

            newRole
        }
        controller.roleService = roleService.createMock()


        assert Role.count() == 2     

        params.authority = 'role3Authority'
        controller.createRole()
        assert Role.count() == 3

        def role = Role.get(3)
        assert response.json.authority == 'role3Authority'
        assert role.authority == 'role3Authority'
        assert role.status == Role.Status.ACTIVE
    }

    void testCreateRoleFail() {
        roleService.demand.createRole(1..2) {authority ->
            def newRole = new Role(authority: authority) 
            if(!newRole.save()) {
                log.error newRole.errors
                throw new RuntimeException("${newRole.errors}")
            }

            newRole
        }
        controller.roleService = roleService.createMock()

        params.authority = 'role1Authority'
        controller.createRole()
        assert response.json.message == 'can not create new role'
    }

    void testShow() {
        roleService.demand.show(1..1) {roleId ->
            def role = Role.get(roleId)
        }
        controller.roleService = roleService.createMock()

        def role = Role.get(1)

        params.id = role.id
        controller.show()
        assert 'role1Authority' == response.json['authority']
    }

    void testUpdate() {
        roleService.demand.update(1..4) {roleParams ->
            def role = Role.get(roleParams.id)

            if(!role) {
                log.error role.errors
                throw new RuntimeException("${role.errors}")
                return
            }

            if (roleParams.version != null) {
                if (role.version > roleParams.version) {
                    println role.version
                    println roleParams.version
                    throw new RuntimeException("${role.errors}")
                    return
                }
            }

            role.properties['authority', 'status'] = roleParams

            if(!role.save()) {
                log.error role.errors
                throw new RuntimeException("${role.errors}")
                return
            }

            [id: role.id, authority: role.authority]
        }
        controller.roleService = roleService.createMock()

        def role = Role.get(1)

        params.id = role.id
        params.version = role.version
        params.authority = 'newRole1Authority'

        controller.update()
        role = Role.findById(role.id)
        assert 'newRole1Authority' == role.authority

        response.reset()
        params.id = role.id
        params.version = -1
        params.authority = 'newNewRole1Authority'
        controller.update()
        assert response.json.message == 'can not edit this role'
        

        params.id = role.id
        params.version = 50
        params.authority = 'newNewRole1Authority'
        controller.update()

        role = Role.findById(role.id)
        assert 'newNewRole1Authority' == role.authority
    }
    
    void testDisableRole() {
        roleService.demand.disable(1..1) {roleId ->
            def role = Role.get(roleId)
            if(role) {
                role.status = Role.Status.INACTIVE
            }

            if(!role.save()) {
                log.error role.errors
                throw new RuntimeException("${role.errors}")
            }

            [id: role.id, status: role.status]
        }
        controller.roleService = roleService.createMock()

        def role = Role.get(1)

        params.id = role.id
        controller.disableRole()

        role = Role.findById(role.id)
        assert role.status == Role.Status.INACTIVE
    }

    void testEnableRole() {
        roleService.demand.enable(1..1) {roleId ->
            def role = Role.get(roleId)
            if(role) {
                role.status = Role.Status.ACTIVE
            }

            if(!role.save()) {
                log.error role.errors
                throw new RuntimeException("${role.errors}")
            }

            [id: role.id, status: role.status]
        }
        controller.roleService = roleService.createMock()

        def role = Role.get(1)

        params.id = role.id
        controller.enableRole()

        role = Role.findById(role.id)
        assert role.status == Role.Status.ACTIVE
    }
    
}
