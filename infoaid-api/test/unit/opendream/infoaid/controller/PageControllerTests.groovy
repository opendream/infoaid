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
/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(PageController)
@Mock([Page, PageService, Users, PageUser, Post, Item, Need])
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
        new PageUser(page: page1, user: user1, relation: PageUser.Relation.OWNER).save()
        new PageUser(page: page1, user: user2, relation: PageUser.Relation.MEMBER).save()
        
        def post = new Post(dateCreated: date, lastUpdated: date, lastActived: date, createdBy: 'nut', updatedBy: 'boy')
        def post2 = new Post(dateCreated: date, lastUpdated: date, lastActived: date+1, createdBy: 'nut', updatedBy: 'boy')
        page1.addToPosts(post)
        page1.addToPosts(post2)

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

    void testMember() {
        pageService.demand.getMembers(1..1) {slug -> Page.findBySlug('slug').users}
        controller.pageService = pageService.createMock()

        params.slug = 'slug'
        controller.member()

        def expectResponse = """[{"id":1,"username":"nut","firstname":"firstname","lastname":"lastname","email":null,"telNo":null,"relation":"OWNER"},{"id":2,"username":"nut2","firstname":"firstname2","lastname":"lastname2","email":null,"telNo":null,"relation":"MEMBER"}]"""
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

        def expectResponse = ''
        assert expectResponse == ''
    }

    void testEmptyInfo() {
        params.slug = 'abc'
        controller.info()
        assert response.text == '{}'
    }
}
