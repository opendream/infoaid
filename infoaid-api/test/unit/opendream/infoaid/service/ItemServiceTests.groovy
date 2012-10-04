package opendream.infoaid.service


import opendream.infoaid.domain.Item
import opendream.infoaid.domain.Need
import opendream.infoaid.domain.Resource
import opendream.infoaid.domain.Page
import opendream.infoaid.domain.PageUser
import opendream.infoaid.domain.User
import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ItemService)
@Mock([Item, Need, Page, PageUser, User, Resource])
class ItemServiceTests {
    def number = 0
    @Before
    void setup() {
        Page.metaClass.generateSlug = {-> delegate.slug = ""+(number++)}
        Page.metaClass.isDirty = {name -> false}
        User.metaClass.encodePassword = { -> 'password'}
        User.metaClass.isDirty = {password -> false}
        def date = new Date()

        def page = new Page(name: "page1", lat: "page1", lng: "page1", 
            dateCreated: date, lastUpdated: date, about: 'this is page 1', 
            picOriginal: 'picOri', picSmall: 'picSma', picLarge: 'picLar').save()

        def user = new User(username: "nut", password: "nut", firstname: 'firstname', lastname: 'lastname', dateCreated: date, lastUpdated: date).save()
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

    void testGetItemHistory()  {
        //slug, itemId, since, until, limit
        def date = new Date()
        def limit = null
        def since, until
        def page = Page.findByName('page1')
        def item = Item.findByName('item1Name')
        def nut = User.findByUsername('nut')
        PageUser.createPage(nut, page)
        service.pageService = new PageService()
        2.times {
            def need = new Need(item: item, lastActived: new Date(), createdBy: nut, updatedBy: nut, expiredDate: new Date() + 7, quantity: 2 + it)
            page.addToPosts(need)
        }

        3.times {
            def resource = new Resource(page: page, item: item, lastActived: date + it +1, createdBy: nut, updatedBy: nut, message: 'message', expiredDate: date+(10+it), quantity: 10+it).save(flush: true)
            page.addToPosts(resource)
        }
        page.save(flush: true)
        
        def result = service.getItemHistory(nut, page.slug, item.id, null, null, since, until, limit)

        assert 5 == result.total
        //assert 2 == result.need.size()
        //assert 3 == result.resources.size()
        assert 2 == result.itemHistory.last().quantity
        assert true == result.authority.isJoined
        assert true == result.authority.isOwner
    }
}
