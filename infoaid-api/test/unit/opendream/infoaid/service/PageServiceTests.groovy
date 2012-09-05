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
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PageService)
@Mock([Page, Post, Comment, Location, PageUser, User, Need, MessagePost, Item])
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
            dateCreated: date, lastUpdated: date, about: 'this is page 1')
        def user1 = new User(username: "nut", password: "nut", firstname: 'firstname', lastname: 'lastname', dateCreated: date, lastUpdated: date).save()
        def user2 = new User(username: "nut2", password: "nut2", firstname: 'firstname2', lastname: 'lastname2').save()
        
        def page2 = new Page(name: "page2", lat: "page2", lng: "page2", 
            dateCreated: date, lastUpdated: date)
        
        def post = new Post(message: 'post1',dateCreated: date, lastUpdated: date, 
            lastActived: date, createdBy: 'nut', updatedBy: 'boy')

        def post2 = new Post(message: 'post2', dateCreated: date, lastUpdated: date, 
            lastActived: date2, createdBy: 'yo', updatedBy: 'boy')
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


    void testCreatePageJoinPageLeavePageInactivePage() {
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

        service.createPage(user.id, name1, lat1, lng1, location, null, null, null)
        service.createPage(user.id, name2, lat2, lng2, null, null, null, null)
        service.createPage(user2.id, name2, lat2, lng2, null, null, null, null) // error because page name is exists

        assert Page.count() == 4

        def page = Page.get(3)
        page.slug = "tmpSlug"
        page.save()
        assert page.location == location

        def page2 = Page.get(4)
        assert page2.location == null

        def pageUser = PageUser.get(1)
        assert pageUser.user == user
        assert pageUser.user != user2
        assert pageUser.relation == PageUser.Relation.OWNER

        service.joinPage(user2.id, "tmpSlug")
        assert page.users.size() == 2

        service.inactivePage(user.id, "tmpSlug")
        def pageStatus = Page.get(page.id).status
        assert pageStatus == Page.Status.INACTIVE

        service.leavePage(user2.id, "tmpSlug")
        assert page.users.size() == 1
    }

    void testGetMembers() {
        def member = service.getMembers("0")
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
        assert topMembers.first() == user6
        assert topMembers.last() == user2
        assert topMembers.size() == 5
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
        def message = 'hello new need'
        def user1 = User.get(1)
        assert page.posts.size() == 22
        def pageUser = new PageUser(page: page, user: user1, relation: PageUser.Relation.MEMBER).save(flush: true)

        service.createNeed(1, "0", message)
        def pageUserAfterCreateNeed = PageUser.get(1)
        assert pageUserAfterCreateNeed.conversation == 1

        page = Page.get(1)
        assert page.posts.size() == 23

    }

    void testCreateMessagePost() {
        def page = Page.get(1)
        def message = 'hello new message'
        def user1 = User.get(1)
        assert page.posts.size() == 22

        def pageUser = new PageUser(page: page, user: user1, relation: PageUser.Relation.MEMBER).save(flush: true)

        def result = service.createMessagePost(1, "0", message)
        def pageUserAfterCreateMessagePost = PageUser.get(1)
        assert pageUserAfterCreateMessagePost.conversation == 1

        page = Page.get(1)
        assert page.posts.size() == 23
        assert user1.id == result.user.id
        assert page.id == result.page.id
        assert message == result.post.message        
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
            lat: 'newLatPage1'
        ]
        service.updatePage("0", data)

        def page = Page.findBySlug("0")
        assert page.name == 'newNamePage1'
        assert page.lng == 'page1'

        data = [
            name: 'newNewNamePage1',
            version: -1
        ]
        service.updatePage("0", data) // version is less than

        assert page.name == 'newNamePage1'
    }

    void testDisablePage() {
        def page = Page.findBySlug("0")
        assert page.status == Page.Status.ACTIVE
        service.disablePage("0")

        page = Page.findBySlug("0")
        assert page.status == Page.Status.INACTIVE
    }
}
