package opendream.infoaid.controller
import opendream.infoaid.domain.Item

import grails.test.mixin.*
import org.junit.*


@TestFor(ItemController)
@Mock(Item)
class ItemControllerTests {

    @Before
    void setup() {
        def item1 = new Item(name: 'item1Name').save(flush: true)
    }

    void testList() {

        def model = controller.list()

        assert model.itemInstanceList.size() == 1
        assert model.itemInstanceTotal == 1
    }

    void testCreateItem() {
        assert Item.count() == 1

        params.name = 'item1Name'
        controller.createItem()
        assert Item.count() == 1

        params.name = ''
        controller.createItem()
        assert Item.count() == 1

        params.name = 'item2Name'
        controller.createItem()
        assert Item.count() == 2
    }

    void testShow() {
        def item = Item.get(1)

        params.id = item.id

        def model = controller.show()

        assert model.itemInstance == item
    }

    void testUpdate() {
        def item = Item.get(1)

        params.id = item.id
        params.version = item.version
        params.name = 'newItem1Name'

        controller.update()
        item = Item.findById(item.id)
        assert 'newItem1Name' == item.name

        params.id = item.id
        params.version = -1
        params.name = 'newNewItem1Name'
        controller.update()

        item = Item.findById(item.id)
        assert 'newItem1Name' == item.name
    }

    void testDisableItem() {
        def item = Item.get(1)

        params.id = item.id
        controller.disableItem()

        item = Item.findById(item.id)
        assert item.status == Item.Status.INACTIVE
    }

}
