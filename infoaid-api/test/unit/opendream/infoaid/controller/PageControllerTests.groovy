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
        Page.metaClass.generateSlug = {-> 'slug'}
        Page.metaClass.isDirty = {name -> false}

        def page1 = new Page(name: "page", lat: "111", lng: "222", dateCreated: date, lastUpdated: date, slug: 'slug').save()
        def user1 = new Users(username: "nut", password: "nut", firstname: 'firstname', lastname: 'lastname', dateCreated: date, lastUpdated: date).save()
        def user2 = new Users(username: "nut2", password: "nut2", firstname: 'firstname2', lastname: 'lastname2').save()
        
        new PageUser(page: page1, user: user1, relation: PageUser.Relation.OWNER, conversation: 1).save()
        new PageUser(page: page1, user: user2, relation: PageUser.Relation.MEMBER, conversation: 2).save()
        
        
        def post = new Post(dateCreated: date, lastUpdated: date, lastActived: date, createdBy: 'nut', updatedBy: 'boy')
        def post2 = new Post(dateCreated: date, lastUpdated: date, lastActived: date+1, createdBy: 'nut', updatedBy: 'boy')
        page1.addToPosts(post)
        page1.addToPosts(post2)

        def comment = new Comment(message: 'comment1')
        def comment2 = new Comment(message: 'comment2')
        post.addToComments(comment)
        post.addToComments(comment2)

        def item = new Item(name: 'item').save()
        def newNeed = new Need(item: item, lastActived: date+2, createdBy: 'nut', updatedBy: 'nut', 
            expiredDate: date, message: 'need1', quantity: 10)
        def newNeed2 = new Need(item: item,lastActived: date+3, createdBy: 'nut', updatedBy: 'nut', 
            expiredDate: date, message: 'need2', quantity: 10)
        page1.addToPosts(newNeed)
        page1.addToPosts(newNeed2)
        page1.save()

        pageService = mockFor(PageService)

    }

    void testInfo() {
        pageService.demand.getInfo(1..1) {slug -> Page.findBySlug(slug)}
        controller.pageService = pageService.createMock()

        params.slug = 'slug'
        
        def expectResponse = """{"id":1,"name":"page","lat":"111","lng":"222","dateCreated":"${date.format(dateFormat)}","lastUpdated":"${date.format(dateFormat)}"}"""
        controller.info()

        assert expectResponse == response.text
    }

    void testMap() {
        pageService.demand.getInfo(1..1) {slug -> Page.findBySlug(slug)}
        controller.pageService = pageService.createMock()

        params.slug = 'slug'
        controller.map()
        def expectResponse = """{"id":1,"name":"page","lat":"111","lng":"222"}"""

        assert expectResponse == response.text
    }

    void testMember() {
        pageService.demand.getMembers(1..1) {slug -> Page.findBySlug('slug').users}
        controller.pageService = pageService.createMock()

        params.slug = 'slug'
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

        params.slug = 'slug'
        controller.topMember()

        def expectResponse = """[{"id":6,"username":"nut6","firstname":"firstname6","lastname":"lastname2","email":null,"telNo":null,"relation":"MEMBER"},{"id":5,"username":"nut5","firstname":"firstname5","lastname":"lastname2","email":null,"telNo":null,"relation":"MEMBER"},{"id":4,"username":"nut4","firstname":"firstname4","lastname":"lastname2","email":null,"telNo":null,"relation":"MEMBER"},{"id":3,"username":"nut3","firstname":"firstname3","lastname":"lastname2","email":null,"telNo":null,"relation":"MEMBER"},{"id":2,"username":"nut2","firstname":"firstname2","lastname":"lastname2","email":null,"telNo":null,"relation":"MEMBER"}]"""
        assert expectResponse == response.text
    }

    void testStatus() {
        pageService.demand.getPosts(1..1) {slug, offset, max -> 
            Page.findBySlug('slug').users
            def posts = Post.createCriteria().list(max: max, sort: 'lastActived', order: 'desc', offset: offset) {
                eq('status', Post.Status.ACTIVE)
                page {
                    eq('slug', slug)
                }
            }

            [posts: posts, totalPosts: posts.totalCount]
        }
        controller.pageService = pageService.createMock()

        params.slug = 'slug'
        params.offset = 0
        params.max = 10
        controller.status()
        def expectResponse = """{"posts":[{"message":"item 10","dateCreated":"${date.format(dateFormat)}","comment":[]},{"message":"item 10","dateCreated":"${date.format(dateFormat)}","comment":[]},{"message":null,"dateCreated":"${date.format(dateFormat)}","comment":[]},{"message":null,"dateCreated":"${date.format(dateFormat)}","comment":["comment1","comment2"]}],"totalPosts":4}"""
        assert expectResponse == response.text
    }

    void testEmptyInfo() {
        params.slug = 'abc'
        controller.info()
        assert response.text == '{}'
    }
}