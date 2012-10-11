import opendream.infoaid.domain.User
import opendream.infoaid.domain.Role
import opendream.infoaid.domain.UserRole
import opendream.infoaid.domain.Item

class BootStrap {

    def init = { servletContext ->

        development {
            if(!User.count()) {
                println "insert user"
                def user = new User(username: 'admin', password: 'password', 
                firstname: 'thawatchai', lastname: 'jong', dateCreated: new Date(), 
                lastUpdated: new Date()).save(flush:true)
                def role = new Role(authority:'ROLE_ADMIN').save(flush:true)
                UserRole.create(user, role)
            }

            initItem()
        }

        production {
            initItem()
        }

    }
    def initItem = {
        def item_list = Item.list()
        item_list.each {
            it.status = Item.Status.INACTIVE
            it.save()
        }

        def focus_item_list = [
            'Tent', 'Clothes', 'Man', 'Gas', 'Fuel', 'Electricity', 'Water',
            'Boat', 'Food', 'Medicine', 'Water Closet', 'Water Pump']

        focus_item_list.each { item_name ->
            def item = Item.createCriteria().get {
                ilike('name', item_name)
            }

            if (! item) {
                item = new Item()
            }

            item.name = item_name
            item.status = Item.Status.ACTIVE

            item.save()
        }
    }

    def destroy = {
    }
}
