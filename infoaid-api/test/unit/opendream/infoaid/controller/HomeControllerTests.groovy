package opendream.infoaid.controller

import opendream.infoaid.domain.Comment
import opendream.infoaid.domain.Page
import opendream.infoaid.domain.PageUser
import opendream.infoaid.domain.PageUser.Relation
import opendream.infoaid.domain.Post
import opendream.infoaid.domain.User
import opendream.infoaid.service.*

import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(HomeController)
@Mock([Page, Post, PageUser, User])
class HomeControllerTests {
    def user
    def follower
    def homeService

    @Before
    void setup() {
        homeService = mockFor(HomeService)
        Page.metaClass.generateSlug = {-> 'slug'}
        Page.metaClass.isDirty = {name -> false}
        User.metaClass.encodePassword = { -> 'password'}
        User.metaClass.isDirty = {password -> false}
        def date = new Date() - 1

        // mock user
        user = new User(username: 'admin', password: 'password', 
            firstname: 'thawatchai', lastname: 'jong')
        user.save(flush:true)

        follower = new User(username: 'follower', password: 'password', 
            firstname: 'nut', lastname: 'tong')
        follower.save(flush:true)

        // mock page
        def page1 = new Page(name: "page1", lat: "page1", 
            lng: "page1", dateCreated: date, lastUpdated: date).save()
        def page2 = new Page(name: "page2", lat: "page2", 
            lng: "page2", dateCreated: date, lastUpdated: date).save()
        def page3 = new Page(name: "page3", lat: "page3", 
            lng: "page3", dateCreated: date, lastUpdated: date).save()

        PageUser.createPage(user, page1, Relation.OWNER)
        PageUser.createPage(user, page2, Relation.OWNER)
        PageUser.createPage(user, page3, Relation.MEMBER)

        PageUser.createPage(follower, page1, Relation.MEMBER)
        PageUser.createPage(follower, page3, Relation.OWNER)


        // mock post (12 posts)
        def firstPost = new Post(message:'first Post', dateCreated: date, lastUpdated: date, 
            lastActived: new Date(), createdBy: 'nut', updatedBy: 'boy', conversation:0)
        page1.addToPosts(firstPost)
        4.times {
            page1.addToPosts(new Post(message:"first Post sub$it", dateCreated: date, lastUpdated: date, 
            lastActived: new Date(), createdBy: "nut", updatedBy: 'boy', conversation:20+it))
        }
        page1.save()

        4.times {
            page2.addToPosts(new Post(message:"second Post$it", dateCreated: date, lastUpdated: date, 
            lastActived: new Date(), createdBy: 'nut', updatedBy: 'boy', conversation:10+it))
        }
        page2.save()

        3.times {
            page3.addToPosts(new Post(message:"third Post$it", dateCreated: date, lastUpdated: date, 
            lastActived: new Date(), createdBy: "nut", updatedBy: 'boy', conversation:1+it))
        }
        page3.save()   

        // update firstPost
        firstPost.lastActived = new Date() 
        page1.addToPosts(firstPost)
        page1.save(fulsh:true)
    }

    void testRecentPostForFirstUser() {
        homeService.demand.getFeedByRecentPost(1..1) {userparam -> getPosts(userparam, 'lastActived')}
        controller.homeService = homeService.createMock()

        params.userId = user.id        
        controller.recentPost()
        assert 10 == response.json.size()
        assert 'first Post' == response.json[0].message
    }

    void testRecentPostForFollowerUser() {
        homeService.demand.getFeedByRecentPost(1..1) {userparam -> getPosts(userparam, 'lastActived')}
        controller.homeService = homeService.createMock()

        params.userId = follower.id
        controller.recentPost()
        assert 8 == response.json.size()
    }

    void testTopPostForFirstUser() {
        homeService.demand.getFeedByTopPost(1..1) {userparam -> getPosts(userparam, 'conversation')}
        controller.homeService = homeService.createMock()

        params.userId = user.id        
        controller.topPost()
        assert 10 == response.json.size()
        assert 'first Post sub3' == response.json[0].message
        assert "first Post sub2" == response.json[1].message
    }

    void testTopPostForFollowerUser() {
        homeService.demand.getFeedByTopPost(1..1) {userparam -> getPosts(userparam, 'conversation')}
        controller.homeService = homeService.createMock()

        params.userId = follower.id     
        controller.topPost()
        assert 8 == response.json.size()        
    }

    def getPosts(userparam, sort) {
        def posts = Post.list([sort:sort, order:'desc', offset:0])
        posts = posts.findAll {
            it.page in userparam.pages
        }
        if(posts.size()>10) {
            posts.subList(0,10)
        } else {
            posts
        } 
    }
}
