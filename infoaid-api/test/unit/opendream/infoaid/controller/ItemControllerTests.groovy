package opendream.infoaid.controller
import opendream.infoaid.domain.Item
import opendream.infoaid.domain.Need
import opendream.infoaid.domain.Page
import opendream.infoaid.domain.PageUser
import opendream.infoaid.domain.Resource
import opendream.infoaid.domain.Comment
import opendream.infoaid.domain.User
import opendream.infoaid.service.ItemService
import opendream.infoaid.service.PageService

import grails.test.mixin.*
import org.junit.*


@TestFor(ItemController)
@Mock([Comment, Item, Need, Page, Resource, PageUser, User])
class ItemControllerTests {

    def itemService

    @Before
    void setup() {
        Page.metaClass.generateSlug = {-> delegate.slug = delegate.name+"-slug"}
        Page.metaClass.isDirty = {name -> false}

        User.metaClass.encodePassword = { -> 'password'}
        User.metaClass.isDirty = {password -> false}

        itemService = mockFor(ItemService)
        def user = new User(username: "nut", password: "nut", firstname: 'firstname', lastname: 'lastname', dateCreated: new Date(), lastUpdated: new Date()).save()
        def page1 = new Page(name: 'page1').save(flush: true)
        PageUser.createPage(user, page1, PageUser.Relation.OWNER)
        
        def item1 = new Item(name: 'item1Name').save(flush: true)
        def item2 = new Item(name: 'item2Name').save(flush: true)

        def need1 = new Need(message: 'need1ja', expiredDate: new Date(), quantity: 11, lastActived: new Date(),
            createdBy: user, updatedBy: user, page: page1, item: item1)
        def need2 = new Need(message: 'need2ja', expiredDate: new Date(), quantity: 22, lastActived: new Date(),
            createdBy: user, updatedBy: user, page: page1, item: item1)

        item1.needs = [need1, need2]
        item1.save(flush: true)
        
    }

    void testList() {
        itemService.demand.list(1..1) {max -> 
            def params = [:]
            params.max = Math.min(max ?: 10, 100)
            def item = Item.list(params)
        }
        controller.itemService = itemService.createMock()

        controller.list()
        assert response.json['totalItems'] == 2
        assert response.json['items'][0].name == 'item1Name'
        assert response.json['items'][0].needs.size() == 2
    }

    void testCreateItem() {
        itemService.demand.createItem(1..2) {name ->
            def newItem = new Item(name: name) 
            if(!newItem.save()) {
                log.error newItem.errors
                throw new RuntimeException("${newItem.errors}")
            }

            newItem
        }
        controller.itemService = itemService.createMock()


        assert Item.count() == 2     

        params.name = 'item3Name'
        controller.createItem()
        assert Item.count() == 3

        def item = Item.get(3)
        assert response.json.name == 'item3Name'
        assert item.name == 'item3Name'
        assert item.status == Item.Status.ACTIVE
    }

    void testCreateItemFail() {
        itemService.demand.createItem(1..2) {name ->
            def newItem = new Item(name: name) 
            if(!newItem.save()) {
                log.error newItem.errors
                throw new RuntimeException("${newItem.errors}")
            }

            newItem
        }
        controller.itemService = itemService.createMock()

        params.name = 'item1Name'
        controller.createItem()
        assert response.json.message == 'can not create new item'
    }

    void testShow() {
        itemService.demand.show(1..1) {itemId ->
            def item = Item.get(itemId)
        }
        controller.itemService = itemService.createMock()

        def item = Item.get(1)

        params.id = item.id
        controller.show()
        println response.text
        assert 'item1Name' == response.json['name']
    }

    void testUpdate() {
        itemService.demand.update(1..4) {itemParams ->
            def item = Item.get(itemParams.id)

            if(!item) {
                log.error item.errors
                throw new RuntimeException("${item.errors}")
                return
            }

            if (itemParams.version != null) {
                if (item.version > itemParams.version) {
                    throw new RuntimeException("${item.errors}")
                    return
                }
            }

            item.properties['name', 'status'] = itemParams

            if(!item.save()) {
                log.error item.errors
                throw new RuntimeException("${item.errors}")
                return
            }

            [id: item.id, name: item.name]
        }
        controller.itemService = itemService.createMock()

        def item = Item.get(1)

        params.id = item.id
        params.version = item.version
        params.name = 'newItem1Name'

        controller.update()
        item = Item.findById(item.id)
        assert 'newItem1Name' == item.name

        response.reset()
        params.id = item.id
        params.version = -1
        params.name = 'newNewItem1Name'
        controller.update()
        assert response.json.message == 'can not edit this item'
        

        params.id = item.id
        params.version = 50
        params.name = 'newNewItem1Name'
        controller.update()

        item = Item.findById(item.id)
        assert 'newNewItem1Name' == item.name
    }
    
    void testDisableItem() {
        itemService.demand.disable(1..1) {itemId ->
            def item = Item.get(itemId)
            if(item) {
                item.status = Item.Status.INACTIVE
            }

            if(!item.save()) {
                log.error item.errors
                throw new RuntimeException("${item.errors}")
            }

            [id: item.id, status: item.status]
        }
        controller.itemService = itemService.createMock()

        def item = Item.get(1)

        params.id = item.id
        controller.disableItem()

        item = Item.findById(item.id)
        assert item.status == Item.Status.INACTIVE
    }

    void testEnableItem() {
        itemService.demand.enable(1..1) {itemId ->
            def item = Item.get(itemId)
            if(item) {
                item.status = Item.Status.ACTIVE
            }

            if(!item.save()) {
                log.error item.errors
                throw new RuntimeException("${item.errors}")
            }

            [id: item.id, status: item.status]
        }
        controller.itemService = itemService.createMock()

        def item = Item.get(1)

        params.id = item.id
        controller.enableItem()

        item = Item.findById(item.id)
        assert item.status == Item.Status.ACTIVE
    }

    void testGetItemHistory() {

        def date = new Date()
        def page = Page.findByName('page1')
        def item = Item.findByName('item1Name')
        def nut = User.findByUsername('nut')

        2.times {
            def need = new Need(item: item, lastActived: new Date(), createdBy: nut, updatedBy: nut, expiredDate: new Date() + 7, quantity: 2)
            page.addToPosts(need)
        }

        3.times {
            def resource = new Resource(page: page, item: item, lastActived: date + it +1, createdBy: nut, updatedBy: nut, message: 'message', expiredDate: date+(10+it), quantity: 10+it).save(flush: true)
            page.addToPosts(resource)
        }
        page.save(flush: true)

        def itemService = new ItemService()
        def pageService = new PageService()
        itemService.pageService = pageService
        itemService.grailsApplication = [config:[infoaid:[api:[post:[max:10]]]]]
        controller.itemService = itemService
        controller.pageService = pageService

        // params user, slug, itemId, fromId, toId, since, until, limit
        params.user = [id:nut.id]
        params.slug = page.slug
        params.itemId = item.id
        params.fromId = null
        params.toId = null
        params.since = null
        params.until = null
        params.limit = null

        controller.itemHistory()

        assert response.json.status == 1
        assert response.json.total == 7
        assert true == response.json.isJoined
        assert true == response.json.isOwner
        //assert response.json.need.size() == 4
        //assert response.json.resource.size() == 3
        //assert response.json.itemHistory.size() == 7
        assert response.json.itemHistory.last().message == """item1Name 11\nneed1ja"""
    }

}
