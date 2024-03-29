package opendream.infoaid.controller

import opendream.infoaid.domain.*
import opendream.infoaid.domain.Item.Status
import opendream.infoaid.service.PageService

import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(FrontPageController)
@Mock([Page, User, PageUser, Post, Item, Need, Comment, PageSummary])
class FrontPageControllerTests {
    def pageService
    def date
    def dateFormat = "yyyy-MM-dd HH:mm"
    @Before
    void setup() {
        date = new Date()
        Page.metaClass.generateSlug = {-> delegate.slug = delegate.name+"-slug"}
        Page.metaClass.isDirty = {name -> false}
        User.metaClass.encodePassword = { -> 'password'}
        User.metaClass.isDirty = {password -> false}
        PageSummary.metaClass.isDirty = {hasNeed -> true}
        pageService = mockFor(PageService)

        def user1 = new User(username: "nut", password: "nut", firstname: 'firstname', lastname: 'lastname', dateCreated: date, lastUpdated: date).save()
        def user2 = new User(username: "nut2", password: "nut2", firstname: 'firstname2', lastname: 'lastname2').save()

        def page1 = new Page(name: "page", lat: "111", lng: "222", dateCreated: date, lastUpdated: date, about: 'this is page 1').save()
        def secondPage = new Page(name: "second-page", lat: "11122", lng: "1234", dateCreated: date, lastUpdated: date, about: 'this is 2nd page').save()
               
        
        def firstPost = new Post(message: 'first post', dateCreated: date, lastUpdated: date, lastActived: date, createdBy: 'nut', updatedBy: 'boy')
        def secondPost = new Post(message: 'second post', dateCreated: date, lastUpdated: date, lastActived: date+1, createdBy: 'nut', updatedBy: 'boy')
        page1.addToPosts(firstPost)
        page1.save()
        page1.addToPosts(secondPost)
        page1.save()

        def comment = new Comment(message: 'comment1', user: user1)
        def comment2 = new Comment(message: 'comment2', user: user1)
        firstPost.addToComments(comment)
        firstPost.addToComments(comment2)

        def item = new Item(name: 'item', status: Status.ACTIVE).save()
        def firstNeed = new Need(item: item, lastActived: date+2, createdBy: 'nut', updatedBy: 'nut', 
            expiredDate: date, message: 'need1', quantity: 9)

        def secondNeed = new Need(item: item,lastActived: date+3, createdBy: 'nut', updatedBy: 'nut', 
            expiredDate: date, quantity: 10)
        page1.addToPosts(firstNeed)
        page1.save()
        page1.addToPosts(secondNeed)
        page1.save(failOnError: true, flush: true)

        def fifthPost = new Post(message: 'fifth post', dateCreated: date, lastUpdated: date, lastActived: date+1, createdBy: 'nut', updatedBy: 'boy')
        secondPage.addToPosts(fifthPost)
        secondPage.save(failOnError: true, flush: true)

        new PageUser(page: page1, user: user1, relation: PageUser.Relation.OWNER, conversation: 1).save()
        new PageUser(page: page1, user: user2, relation: PageUser.Relation.MEMBER, conversation: 2).save()
        new PageUser(page: secondPage, user: user1, relation: PageUser.Relation.OWNER, conversation: 1).save()
    }

    void testInfo() {
        pageService = new PageService()
        pageService.grailsApplication = [config:[infoaid:[api:[need:[max:5]]]]]
        def page2 = new Page(name: "page2", lat: "latPage2", lng: "lngPage2", dateCreated: date, lastUpdated: date, slug: 'slug2', about: 'this is page 2').save()
        pageService.createOrUpdatePageSummary(page2)

        def page = Page.findByName('page')
        pageService.createOrUpdatePageSummary(page)
        pageService.addPageSummaryItems(page, [[id: 1, name: 'a', need: 10, resource: 10]])

        page = Page.findByName('second-page')
        pageService.createOrUpdatePageSummary(page)
        pageService.addPageSummaryItems(page, [[id: 1, name: 'a', need: 30, resource: 10]])

        controller.pageService = pageService

        controller.info()

        println response.json
        Page.list().each {
            println " page ${it.name}"
        }

        assert 3 == Page.count()
        assert 2 == response.json['totalPages']
        assert '111' == response.json['pages'][0].lat
        //def expectResponse = """{"status":1,"pages":[{"name":"page","lat":"111","lng":"222","needs":[{"message":"item 10","quantity":10},{"message":"item 9","quantity":9}]},{"name":"second-page","lat":"11122","lng":"1234","needs":[]},{"name":"page2","lat":"latPage2","lng":"lngPage2","needs":[]}],"totalPages":2}"""
        //assert expectResponse == response.text
        assert 2 == response.json.pages.size()
        assert 'page' == response.json.pages[0].name
        //assert 'second-page' == response.json.pages[1].name

    }
}
