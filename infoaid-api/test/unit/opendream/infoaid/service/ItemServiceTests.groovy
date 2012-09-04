package opendream.infoaid.service


import opendream.infoaid.domain.Item
import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ItemService)
@Mock([Item])
class ItemServiceTests {

    @Before
    void setup() {
        def item1 = new Item(name: 'item1Name').save(flush: true)
        def item2 = new Item(name: 'item2Name').save(flush: true)
    }

    void testList() {
        def itemList = service.list()
        assert itemList.size() == 2
        assert itemList.first() == Item.get(1)
    }

    void testCreateItem() {
    	assert Item.count() == 2

    	service.createItem('item3Name')

    	assert Item.count() == 3
    	assert Item.list().last().name == 'item3Name'
    }

    void testCreateItemFail() {
    	assert Item.count() == 2

    	assert Item.count() == 2
    	shouldFail(RuntimeException) {
            service.createItem('')
            service.createItem('item1Name')
        }
    }
    
    void testShow() {
    	def item = service.show(1)
    	assert item == Item.get(1)
    }

    void testUpdate() {
    	def itemParams = [id: 1,name: 'nut', status: Item.Status.INACTIVE]
        service.update(itemParams)

        def item1 = Item.get(1)

        assert item1.name == 'nut'
    }

    void testUpdateFail() {
    	def itemParams = [id: 1, name: '', status: Item.Status.INACTIVE]
    	shouldFail(RuntimeException) {
        	service.update(itemParams)
        }

        def itemParams2 = [id: 1, name: 'abc', version: -1]

        shouldFail(RuntimeException) {
        	service.update(itemParams2)
        }
    }

    void testDisable() {
    	def itemId = 1
    	service.disable(itemId)

    	def item = Item.get(itemId)
    	assert item.status == Item.Status.INACTIVE
    }

    void testEnable() {
    	def itemId = 1
    	service.enable(itemId)

    	def item = Item.get(itemId)
    	assert item.status == Item.Status.ACTIVE
    }
}
