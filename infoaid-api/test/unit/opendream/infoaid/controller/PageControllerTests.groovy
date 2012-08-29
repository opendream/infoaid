package opendream.infoaid.controller

import grails.test.mixin.*
import org.junit.*
import opendream.infoaid.domain.Page
import opendream.infoaid.domain.Users
import opendream.infoaid.domain.PageUser
import opendream.infoaid.service.PageService
import opendream.infoaid.domain.Post
import opendream.infoaid.domain.Item
import opendream.infoaid.domain.Need
import opendream.infoaid.domain.Comment
/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(PageController)
@Mock([Page, PageService, Users, PageUser, Post, Item, Need, Comment])
class PageControllerTests {
    def pageService
    def date
    def dateFormat = "yyyy-MM-dd HH:mm"
    @Before
    void setup() {
        date = new Date()
        Page.metaClass.generateSlug = {-> delegate.slug = delegate.name+"-slug"}
        Page.metaClass.isDirty = {name -> false}
        pageService = mockFor(PageService)

        def user1 = new Users(username: "nut", password: "nut", firstname: 'firstname', lastname: 'lastname', dateCreated: date, lastUpdated: date).save()
        def user2 = new Users(username: "nut2", password: "nut2", firstname: 'firstname2', lastname: 'lastname2').save()

        def page1 = new Page(name: "page", lat: "111", lng: "222", dateCreated: date, lastUpdated: date, slug: 'slug', about: 'this is page 1').save()
        def secondPage = new Page(name: "second-page", lat: "11122", lng: "1234", dateCreated: date, lastUpdated: date, slug: 'slug', about: 'this is 2nd page').save()
               
        
        def firstPost = new Post(message: 'first post', dateCreated: date, lastUpdated: date, lastActived: date, createdBy: 'nut', updatedBy: 'boy')
        def secondPost = new Post(message: 'second post', dateCreated: date, lastUpdated: date, lastActived: date+1, createdBy: 'nut', updatedBy: 'boy')
        page1.addToPosts(firstPost)
        page1.save()
        page1.addToPosts(secondPost)
        page1.save()

        def comment = new Comment(message: 'comment1')
        def comment2 = new Comment(message: 'comment2')
        firstPost.addToComments(comment)
        firstPost.addToComments(comment2)

        def item = new Item(name: 'item').save()
        def firstNeed = new Need(item: item, lastActived: date+2, createdBy: 'nut', updatedBy: 'nut', 
            expiredDate: date, message: 'need1', quantity: 9)

        def secondNeed = new Need(item: item,lastActived: date+3, createdBy: 'nut', updatedBy: 'nut', 
            expiredDate: date, quantity: 10)
        page1.addToPosts(firstNeed)
        page1.save()
        page1.addToPosts(secondNeed)
        page1.save()

        def fifthPost = new Post(message: 'fifth post', dateCreated: date, lastUpdated: date, lastActived: date+1, createdBy: 'nut', updatedBy: 'boy')
        secondPage.addToPosts(fifthPost)
        secondPage.save()

        new PageUser(page: page1, user: user1, relation: PageUser.Relation.OWNER, conversation: 1).save()
        new PageUser(page: page1, user: user2, relation: PageUser.Relation.MEMBER, conversation: 2).save()
        new PageUser(page: secondPage, user: user1, relation: PageUser.Relation.OWNER, conversation: 1).save()
    }

    void testInfo() {
        pageService.demand.getInfo(1..1) {slug -> Page.findBySlug(slug)}
        controller.pageService = pageService.createMock()

        params.slug = 'page-slug'
        
        def expectResponse = """{"id":1,"name":"page","lat":"111","lng":"222","dateCreated":"${date.format(dateFormat)}","lastUpdated":"${date.format(dateFormat)}"}"""
        controller.info()

        assert expectResponse == response.text
    }

    void testMap() {
        pageService.demand.getInfo(1..1) {slug -> Page.findBySlug(slug)}
        controller.pageService = pageService.createMock()

        params.slug = 'page-slug'
        controller.map()
        def expectResponse = """{"id":1,"name":"page","lat":"111","lng":"222"}"""

        assert expectResponse == response.text
    }

    void testMember() {
        pageService.demand.getMembers(1..1) {slug -> Page.findBySlug(slug).users}
        controller.pageService = pageService.createMock()

        params.slug = 'page-slug'
        controller.member()

        def expectResponse = """[{"id":1,"username":"nut","firstname":"firstname","lastname":"lastname","email":null,"telNo":null,"relation":"OWNER"},{"id":2,"username":"nut2","firstname":"firstname2","lastname":"lastname2","email":null,"telNo":null,"relation":"MEMBER"}]"""
        assert expectResponse == response.text
    }

    void testTopMember() {
        def page1 = Page.get(1)
        def user3 = new Users(username: "nut3", password: "nut3", firstname: 'firstname3', lastname: 'lastname2').save()
        def user4 = new Users(username: "nut4", password: "nut4", firstname: 'firstname4', lastname: 'lastname2').save()
        def user5 = new Users(username: "nut5", password: "nut5", firstname: 'firstname5', lastname: 'lastname2').save()
        def user6 = new Users(username: "nut6", password: "nut6", firstname: 'firstname6', lastname: 'lastname2').save()
        new PageUser(page: page1, user: user3, relation: PageUser.Relation.MEMBER, conversation: 3).save()
        new PageUser(page: page1, user: user4, relation: PageUser.Relation.MEMBER, conversation: 4).save()
        new PageUser(page: page1, user: user5, relation: PageUser.Relation.MEMBER, conversation: 5).save()
        new PageUser(page: page1, user: user6, relation: PageUser.Relation.MEMBER, conversation: 6).save()

        pageService.demand.getTopMembers(1..1) {slug -> 
            def page = Page.findBySlug(slug)
            PageUser.createCriteria().list(sort: 'conversation', order: 'desc', max: 5) {
                eq('page', page)
            }
        }
        controller.pageService = pageService.createMock()

        params.slug = 'page-slug'
        controller.topMember()

        def expectResponse = """[{"id":6,"username":"nut6","firstname":"firstname6","lastname":"lastname2","email":null,"telNo":null,"relation":"MEMBER"},{"id":5,"username":"nut5","firstname":"firstname5","lastname":"lastname2","email":null,"telNo":null,"relation":"MEMBER"},{"id":4,"username":"nut4","firstname":"firstname4","lastname":"lastname2","email":null,"telNo":null,"relation":"MEMBER"},{"id":3,"username":"nut3","firstname":"firstname3","lastname":"lastname2","email":null,"telNo":null,"relation":"MEMBER"},{"id":2,"username":"nut2","firstname":"firstname2","lastname":"lastname2","email":null,"telNo":null,"relation":"MEMBER"}]"""
        assert expectResponse == response.text
    }

    void testStatus() {
        pageService.demand.getPosts(1..1) {slug, offset, max -> 
            def posts = Post.createCriteria().list(max: max, sort: 'lastActived', order: 'desc', offset: offset) {
                eq('status', Post.Status.ACTIVE)
                page {
                    eq('slug', slug)
                }
            }
            posts
            //[posts: posts, totalPosts: posts.totalCount]
            //getData()
        }
        controller.pageService = pageService.createMock()

        params.slug = 'page-slug'
        params.offset = 0
        params.max = 10
        controller.status()
        //def expectResponse = """{"posts":[{"message":"item 10","dateCreated":"${date.format(dateFormat)}","comments":[]},{"message":"item 10","dateCreated":"${date.format(dateFormat)}","comments":[]},{"message":null,"dateCreated":"${date.format(dateFormat)}","comments":[]},{"message":null,"dateCreated":"${date.format(dateFormat)}","comments":["comment1","comment2"]}],"totalPosts":4}"""
        //assert expectResponse == response.text
        assert 4 == response.json.size()
        assert 'item 10' == response.json[0].message
    }

    void testEmptyInfo() {
        params.slug = 'abc'
        controller.info()
        assert response.text == '{}'
    }

    void testNeed() {
        pageService.demand.getAllNeeds(1..1) {slug -> 
            def page = Page.findBySlug(slug)
            def needs = Need.findAllByPageAndStatus(page, Post.Status.ACTIVE)

            [needs: needs, totalNeeds: needs.size()]
        }

        controller.pageService = pageService.createMock()

        params.slug = 'page-slug'
        controller.need()

        def expectResponse = """{"needs":[{"message":"item 9","dateCreated":"${(date).format(dateFormat)}","createdBy":"nut","expiredDate":"${(date).format(dateFormat)}","quantity":9,"item":"item"},{"message":"item 10","dateCreated":"${(date).format(dateFormat)}","createdBy":"nut","expiredDate":"${(date).format(dateFormat)}","quantity":10,"item":"item"}],"totalNeeds":2}"""
        assert expectResponse == response.text
    }

    void testLimitNeed() {
        pageService.demand.getLimitNeeds(1..1) {slug, max -> 
            def page = Page.findBySlug(slug)
            def needs = Need.createCriteria().list(max: max, sort: 'dateCreated', order: 'desc') {
                eq('status', Post.Status.ACTIVE)
                eq('page', page)
            }

            [needs: needs, totalNeeds: needs.totalCount]
        }

        controller.pageService = pageService.createMock()

        params.slug = 'page-slug'
        params.limit = 1
        controller.limitNeed()

        def expectResponse = """{"needs":[{"message":"item 10","dateCreated":"${(date).format(dateFormat)}","createdBy":"nut","expiredDate":"${(date).format(dateFormat)}","quantity":10,"item":"item"}],"totalNeeds":2}"""
        assert expectResponse == response.text
    }

    void testAbout() {
        pageService.demand.getAbout(1..1) {slug -> Page.findBySlug(slug).about}
        controller.pageService = pageService.createMock()

        params.slug = 'page-slug'
        controller.about()

        def expectResponse = 'this is page 1'
        assert expectResponse == response.text
    }

    void testJoinUs() {
        def user3 = new Users(username: "nut3", password: "nut3", firstname: 'firstname3', lastname: 'lastname3').save(flush: true)
        def testPage = Page.findBySlug('page-slug')
        assert 2 == testPage.users.size()
        pageService.demand.joinPage(1..1) {userId, slug -> 
            def user = Users.get(userId)
            def page = Page.findBySlug(slug)
            new PageUser(user: user, page: page, relation: PageUser.Relation.MEMBER).save(flush: true)
        }
        controller.pageService = pageService.createMock()

        params.userId = 3
        params.slug = 'page-slug'

        controller.joinUs()

        testPage = Page.findBySlug('page-slug')
        assert 3 == testPage.users.size()
        assert PageUser.Relation.MEMBER == testPage.users.last().relation
    }    

    void testTopPost() {
        def postlist = Post.list()
        4.times {
            postlist[it].conversation = it
            postlist[it].save()
        }

        pageService.demand.getTopPost(1..1) { slug, offset -> 
            def posts = Post.createCriteria().list(max: 10, sort: 'conversation', order: 'desc', offset: offset) {
                eq('status', Post.Status.ACTIVE)
                page {
                    eq('slug', slug)
                }
            }
            posts            
        }
        controller.pageService = pageService.createMock()

        params.slug = 'page-slug'
        params.offset = 0
        controller.topPost()
        
        assert 4 == response.json.size()
        assert 'item 10' == response.json[0].message
        assert 'item 9' == response.json[1].message
        assert 'first post' == response.json[3].message
    }
}