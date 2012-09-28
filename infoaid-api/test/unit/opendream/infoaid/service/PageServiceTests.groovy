package opendream.infoaid.service

import grails.test.mixin.*
import org.junit.*

import opendream.infoaid.domain.Page
import opendream.infoaid.domain.Post
import opendream.infoaid.domain.Comment
import opendream.infoaid.domain.Location
import opendream.infoaid.domain.PageUser
import opendream.infoaid.domain.User
import opendream.infoaid.domain.Need
import opendream.infoaid.domain.MessagePost
import opendream.infoaid.domain.Item
import opendream.infoaid.domain.Resource

import grails.validation.ValidationException
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PageService)
@Mock([Page, Post, Comment, Location, PageUser, User, Need, MessagePost, Item, Resource])
class PageServiceTests {
    def date
    def number = 0

    @Before
    void setup() {

        Page.metaClass.generateSlug = {-> delegate.slug = ""+(number++)}
        Page.metaClass.isDirty = {name -> false}
        User.metaClass.encodePassword = { -> 'password'}
        User.metaClass.isDirty = {password -> false}
        date = new Date()-19
        def date2 = new Date()-20
        
        def page = new Page(name: "page1", lat: "page1", lng: "page1", 
            dateCreated: date, lastUpdated: date, about: 'this is page 1', 
            picOriginal: 'picOri', picSmall: 'picSma', picLarge: 'picLar')
        def user1 = new User(username: "nut", password: "nut", firstname: 'firstname', lastname: 'lastname', dateCreated: date, lastUpdated: date).save()
        def user2 = new User(username: "nut2", password: "nut2", firstname: 'firstname2', lastname: 'lastname2').save()
        
        def page2 = new Page(name: "page2", lat: "page2", lng: "page2", 
            dateCreated: date, lastUpdated: date)
        
        def post = new Post(message: 'post1',dateCreated: date, lastUpdated: date, 
            lastActived: date, createdBy: user1, updatedBy: user2)

        def post2 = new Post(message: 'post2', dateCreated: date, lastUpdated: date, 
            lastActived: date2, createdBy: user2, updatedBy: user1)
        page.addToPosts(post)
        page.addToPosts(post2)

        20.times {
            def post3 = new Post(message: 'post'+it, dateCreated: date, lastUpdated: date, 
                lastActived: date2+it, createdBy: 'yo'+it, updatedBy: 'boy')
            page.addToPosts(post3)
        }
        page.save()
        page2.save()
    }

    void testGetInfo() {
        def info = service.getInfo(0)
        
        assert info.name == 'page1'
        assert info.lat == 'page1'
        assert info.lng == 'page1'
    }      


    void testCreatePageJoinPageLeavePageInactivePageSetRelationRemoveUserFromPageIsOwner() {
        def user = new User(username: 'admin', password: 'password', firstname: 'thawatchai', lastname: 'jong')
        user.save()
        def user2 = new User(username: 'admin2', password: 'password2', firstname: 'jong', lastname: 'thawatchai')
        user2.save()

        def name1 = 'testCreatePage1'
        def lat1 = 'lat1'
        def lng1 = 'lng1'
        def location = new Location(region: 'region1', province: 'province1', district: 'subDistrict1', label: 'label1')

        def name2 = 'testCreatePage2'
        def lat2 = 'lat2'
        def lng2 = 'lng2'

        service.createPage(user.id, name1, lat1, lng1, location, null, null, null, 'picOri', 'picSmall', 'picLarge')
        service.createPage(user.id, name2, lat2, lng2, null, null, null, null, null, null, null)

        assert Page.count() == 4

        def page = Page.get(3)
        page.slug = "tmpSlug"
        page.save()
        assert page.location == location
        assert page.picOriginal == 'picOri'

        def isOwner = service.isOwner(user.id, 'tmpSlug')
        assert isOwner.isOwner == true

        def page2 = Page.get(4)
        assert page2.location == null

        def pageUser = PageUser.get(1)
        assert pageUser.user == user
        assert pageUser.user != user2
        assert pageUser.relation == PageUser.Relation.OWNER

        service.joinPage(user2.id, "tmpSlug")
        assert page.getUsers(0).size() == 2
        def isJoined = service.isJoined(user2.id, 'tmpSlug')
        assert isJoined.isJoined == true
        isJoined = service.isJoined(123456789, 'tmpSlug')
        assert isJoined.isJoined == false
        

        service.inactivePage(user.id, "tmpSlug")
        def pageStatus = Page.get(page.id).status
        assert pageStatus == Page.Status.INACTIVE

        service.leavePage(user2.id, "tmpSlug")
        assert page.getUsers(0).size() == 1

        service.setRelation(user.id, 'tmpSlug', 'Member')
        pageUser = PageUser.get(1)
        assert pageUser.relation == PageUser.Relation.MEMBER

        service.removeUserFromPage(user.id, "tmpSlug")
        assert page.getUsers(0).size() == 0
    }

    void testCreatePageFail() {
        def user = new User(username: 'admin', password: 'password', firstname: 'thawatchai', lastname: 'jong')
        user.save()
        def user2 = new User(username: 'admin2', password: 'password2', firstname: 'jong', lastname: 'thawatchai')
        user2.save()

        def name2 = 'testCreatePage2'
        def lat2 = 'lat2'
        def lng2 = 'lng2'

        service.createPage(user.id, name2, lat2, lng2, null, null, null, null, null, null, null)
        shouldFail(ValidationException) {
            service.createPage(user2.id, name2, lat2, lng2, null, null, null, null, null, null, null)
        }
    }

    void testGetMembers() {
        def member = service.getMembers("0", 0, 0)
        assert member.size() == 0
    }

    void testGetTopMembers() {
        def page = Page.get(1)
        def user1 = User.get(1)
        def user2 = User.get(2)
        def user3 = new User(username: "nut3", password: "nut2", firstname: 'firstname2', lastname: 'lastname2').save(flush: true)
        def user4 = new User(username: "nut4", password: "nut2", firstname: 'firstname2', lastname: 'lastname2').save(flush: true)
        def user5 = new User(username: "nut5", password: "nut2", firstname: 'firstname2', lastname: 'lastname2').save(flush: true)
        def user6 = new User(username: "nut6", password: "nut2", firstname: 'firstname2', lastname: 'lastname2').save(flush: true)

        new PageUser(page: page, user: user1, relation: PageUser.Relation.MEMBER, conversation: 1).save(flush: true)
        new PageUser(page: page, user: user2, relation: PageUser.Relation.MEMBER, conversation: 2).save(flush: true)
        new PageUser(page: page, user: user3, relation: PageUser.Relation.MEMBER, conversation: 3).save(flush: true)
        new PageUser(page: page, user: user4, relation: PageUser.Relation.MEMBER, conversation: 4).save(flush: true)
        new PageUser(page: page, user: user5, relation: PageUser.Relation.MEMBER, conversation: 5).save(flush: true)
        new PageUser(page: page, user: user6, relation: PageUser.Relation.MEMBER, conversation: 6).save(flush: true)

        def topMembers = service.getTopMembers("0")
        assert topMembers.pageUsers.user.first() == user6
        assert topMembers.pageUsers.user.last() == user2
        assert topMembers.pageUsers.user.size() == 5
    }

    void testGetAllNeeds() {
        def item = new Item(name: 'item')
        def newNeed = new Need(item: item, lastActived: date, createdBy: 'nut', updatedBy: 'nut', expiredDate: date, message: 'message', quantity: 10)
        def newNeed2 = new Need(item: item, lastActived: date, createdBy: 'nut', updatedBy: 'nut', expiredDate: date, message: 'message', quantity: 10)

        def page = Page.get(1)
        page.addToPosts(newNeed)
        page.addToPosts(newNeed2)
        page.save()
        def results = service.getAllNeeds('0')
        assert results.totalNeeds == 2

        page = Page.get(1)
        newNeed2.status = Post.Status.INACTIVE
        page.addToPosts(newNeed2)
        page.save()

        results = service.getAllNeeds('0')
        assert results.totalNeeds == 1
    }

    void testGetLimitNeeds() {
        def item = new Item(name: 'item')
        def newNeed = new Need(item: item, lastActived: date, createdBy: 'nut', updatedBy: 'nut', expiredDate: date, quantity: 10)
        def newNeed2 = new Need(item: item, lastActived: date, createdBy: 'nut', updatedBy: 'nut', expiredDate: date, quantity: 10)
        def newNeed3 = new Need(item: item, lastActived: date, createdBy: 'nut', updatedBy: 'nut', expiredDate: date, quantity: 10)

        def page = Page.get(1)
        page.addToPosts(newNeed)
        page.addToPosts(newNeed2)
        page.save()

        def results = service.getLimitNeeds('0', 2)
        assert results.needs.size() == 2
        assert results.totalNeeds == 2
    }

    void testCreateNeed() {
        def page = Page.get(1)
        def item = new Item(name: 'item').save()
        def quantity = 10
        def message = 'hello new need'
        def user1 = User.get(1)
        assert page.posts.size() == 22
        def pageUser = new PageUser(page: page, user: user1, relation: PageUser.Relation.MEMBER).save(flush: true)

        def result = service.createNeed(user1.id, page.slug, item.id, quantity, message)
        def pageUserAfterCreateNeed = PageUser.get(1)
        assert pageUserAfterCreateNeed.conversation == 1

        page = Page.get(1)
        assert page.posts.size() == 23

        assert user1.id == result.user.id
        assert page.id == result.page.id
        assert item.id == result.post.item.id
        assert quantity == result.post.quantity
    }

    void testCreateMessagePost() {
        def page = Page.get(1)
        def message = 'hello new message'
        def user1 = User.get(1)
        assert page.posts.size() == 22

        def pageUser = new PageUser(page: page, user: user1, relation: PageUser.Relation.MEMBER).save(flush: true)

        def result = service.createMessagePost(1, "0", message, 'picOri', 'picSma')
        def pageUserAfterCreateMessagePost = PageUser.get(1)
        assert pageUserAfterCreateMessagePost.conversation == 1

        page = Page.get(1)
        assert page.posts.size() == 23
        assert user1.id == result.user.id
        assert page.id == result.page.id
        assert message == result.post.message
        assert 'picOri' == result.post.picOriginal
        assert 'picSma' == result.post.picSmall
    }

    void testGetAbout() {
        def about = service.getAbout("0") // slug = 0
        assert about == 'this is page 1'
    }

    void testGetSummaryInfo() {
        def item = new Item(name: 'item')
        def newNeed = new Need(item: item, lastActived: date, createdBy: 'nut', updatedBy: 'nut', 
            expiredDate: date, message: 'message', quantity: 10)
        def newNeed2 = new Need(item: item, lastActived: date, createdBy: 'nut', updatedBy: 'nut', 
            expiredDate: date, message: 'message', quantity: 10)

        def page = Page.get(1)
        page.addToPosts(newNeed)
        page.addToPosts(newNeed2)
        page.save()

        def summaryInfo = service.getSummaryInfo()

        assert summaryInfo.size() == 2 
    }

    void testUpdatePage() {
        def data = [
            name: 'newNamePage1',
            lat: 'newLatPage1',
            picOriginal: 'picOriginal2'
        ]
        service.updatePage("0", data)

        def page = Page.findBySlug("0")
        assert page.name == 'newNamePage1'
        assert page.lng == 'page1'
        assert page.picOriginal == 'picOriginal2'

        data = [
            name: 'newNewNamePage1',
            version: -1
        ]
        service.updatePage("0", data) // version is less than

        assert page.name == 'newNamePage1'
    }

    void testDisablePageEnablePage() {
        def page = Page.findBySlug("0")
        assert page.status == Page.Status.ACTIVE
        def result = service.disablePage("0")

        assert result.status == Page.Status.INACTIVE

        page = Page.findBySlug("0")
        assert page.status == Page.Status.INACTIVE

        service.enablePage("0")
        page = Page.findBySlug("0")
        assert page.status == Page.Status.ACTIVE
    }

    void testGetActiveNeedPage() {
        def item = new Item(name: 'item')
        def newNeed = new Need(item: item, lastActived: date, createdBy: 'nut', updatedBy: 'nut', expiredDate: date, message: 'message', quantity: 10)
        def newNeed2 = new Need(item: item, lastActived: date, createdBy: 'nut', updatedBy: 'nut', expiredDate: date, message: 'message', quantity: 10)

        def page = Page.get(1)
        page.addToPosts(newNeed)        
        page.addToPosts(newNeed2)
        page.save()

        def page2 = Page.get(2)
        page2.addToPosts(newNeed2)
        page2.save()

        def pages = service.getActiveNeedPage()
        assert 2 == pages.size()
        assert 24 == pages[0].posts.size()
        assert 'page1' == pages[0].name
        assert 'page2' == pages[1].name
        assert 1 == pages[1].posts.size()
    }

    void testSearchPage() {
        def pages = service.searchPage('page1')
        assert pages.size() == 1
        assert pages[0].name == 'page1'

        pages = service.searchPage('2')
        assert pages.size() == 1
        assert pages[0].name == 'page2'

        pages = service.searchPage('This')
        assert pages.size() == 1

        pages = service.searchPage('')
        assert pages.size() == 2
        assert pages[0].name == 'page2'
        assert pages[1].name == 'page1'
    }

    void testGetResource() {
        def user = User.findByUsername('nut')
        def item = new Item(name: 'item').save(flush: true)
        def page = Page.findBySlug('0')
        def page2 = Page.findBySlug('1')
        def newResource = new Resource(page: page, item: item, lastActived: date, createdBy: user, updatedBy: user, expiredDate: date, message: 'message', quantity: 10).save(flush: true)
        def newResource2 = new Resource(page: page, item: item, lastActived: date, createdBy: user, updatedBy: user, expiredDate: date, message: 'message', quantity: 10).save(flush: true)
        def params = [slug:'0']
        def result = service.getResource(params)

        assert result.resources[0] == newResource
        assert result.totalResources == 2

        def newResource3 = new Resource(page: page2, item: item, lastActived: date, createdBy: user, updatedBy: user, expiredDate: date, message: 'message', quantity: 10).save(flush: true)

        params = [slug: '1']

        result = service.getResource(params)
        assert result.totalResources == 1

        params = [since: new Date()-30]
        result = service.getResource(params)
        assert result.totalResources == 3

        params = [since: new Date()]
        result = service.getResource(params)
        assert result.totalResources == 0

        params = [until: new Date()-30]
        result = service.getResource(params)
        assert result.totalResources == 0

        params = [slug: '0', until: new Date()]
        result = service.getResource(params)
        assert result.totalResources == 2

        params = [fromId: 1234545555]
        result = service.getResource(params)
        assert result.totalResources == 0

        params = [toId: 12345666]
        result = service.getResource(params)
        assert result.totalResources == 3

        params = [slug: '1', toId: 10101010101]
        result = service.getResource(params)
        assert result.totalResources == 1

        params = [max: 1]
        result = service.getResource(params)
        assert result.totalResources == 1

        params = [itemName: 'item']
        result = service.getResource(params)
        assert result.totalResources == 3

        params = [userId: user.id]
        result = service.getResource(params)
        assert result.totalResources == 3

        params = [sort: 'dateCreated', order: 'desc']
        result = service.getResource(params)
        assert result.totalResources == 3
        assert result.resources[0] == newResource3

        params = [sort: 'dateCreated', order: 'asc']
        result = service.getResource(params)
        assert result.totalResources == 3
        assert result.resources[0] == newResource
    }

    void testCreateResource() {
        assert Resource.count() == 0
        def user = User.findByUsername('nut')
        def item = new Item(name: 'item').save(flush: true)
        def page = Page.findBySlug('0')
        def page2 = Page.findBySlug('1')

        def pageUser = new PageUser(page: page, user: user, relation: PageUser.Relation.OWNER, conversation: 1).save()

        def params = [:]
        def result = service.createResource(params)
        assert result.status == 0
        assert result.message == 'User Id not found'

        params = [userId: user.id]
        result = service.createResource(params)
        assert result.status == 0
        assert result.message == 'Page not found'

        params = [userId: user.id, slug: page.slug]
        result = service.createResource(params)
        assert result.message == 'Item not found'

        params = [userId: user.id, slug: page.slug, itemId: item.id]
        result = service.createResource(params)
        assert result.status == 1
        assert result.user == user
        assert result.page == page
        assert result.post.quantity == 0
        assert result.pageUser == pageUser
        println result

        assert Resource.count() == 1
    }
}
