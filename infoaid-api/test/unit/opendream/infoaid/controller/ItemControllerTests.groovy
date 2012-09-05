package opendream.infoaid.controller
import opendream.infoaid.domain.Item
import opendream.infoaid.domain.Need
import opendream.infoaid.domain.Page
import opendream.infoaid.service.ItemService

import grails.test.mixin.*
import org.junit.*


@TestFor(ItemController)
@Mock([Item, Need, Page])
class ItemControllerTests {

    def itemService

    @Before
    void setup() {
        Page.metaClass.generateSlug = {-> delegate.slug = delegate.name+"-slug"}

        Page.metaClass.isDirty = {name -> false}

        itemService = mockFor(ItemService)

        def page1 = new Page(name: 'page1').save(flush: true)
        
        def item1 = new Item(name: 'item1Name').save(flush: true)
        def item2 = new Item(name: 'item2Name').save(flush: true)

        def need1 = new Need(message: 'need1ja', expiredDate: new Date(), quantity: 11, lastActived: new Date(),
            createdBy: 'nut1', updatedBy: 'nut11', page: page1, item: item1)
        def need2 = new Need(message: 'need2ja', expiredDate: new Date(), quantity: 22, lastActived: new Date(),
            createdBy: 'nut2', updatedBy: 'nut22', page: page1, item: item1)

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

}
