package opendream.infoaid.service

import opendream.infoaid.domain.Comment
import opendream.infoaid.domain.Page
import opendream.infoaid.domain.PageUser
import opendream.infoaid.domain.PageUser.Relation
import opendream.infoaid.domain.Post
import opendream.infoaid.domain.User

import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(HomeService)
@Mock([Page, Post, PageUser, User])
class HomeServiceTests {
    def user
    def follower    

    @Before
    void setUp() {
        Page.metaClass.generateSlug = {-> 'slug'}
        Page.metaClass.isDirty = {name -> false}
        User.metaClass.encodePassword = { -> 'password'}
        User.metaClass.isDirty = {password -> false}
        def date = new Date() - 1

        // mock user
        user = new User(username: 'admin', password: 'password', 
            firstname: 'thawatchai', lastname: 'jong', dateCreated: date, lastUpdated: date)
        user.save(flush:true)

        follower = new User(username: 'follower', password: 'password', 
            firstname: 'nut', lastname: 'tong', dateCreated: date, lastUpdated: date)
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
        page1.save(flush: true)
        4.times {
            page1.addToPosts(new Post(message:"first Post sub$it", dateCreated: date, lastUpdated: date, 
            lastActived: new Date(), createdBy: "nut", updatedBy: 'boy', conversation:20+it))
            page1.save(flush: true)
        }
        

        4.times {
            page2.addToPosts(new Post(message:"second Post$it", dateCreated: date, lastUpdated: date, 
            lastActived: new Date(), createdBy: 'nut', updatedBy: 'boy', conversation:10+it))
            page2.save(flush: true)
        }
        

        3.times {
            page3.addToPosts(new Post(message:"third Post$it", dateCreated: date, lastUpdated: date, 
            lastActived: new Date(), createdBy: "nut", updatedBy: 'boy', conversation:1+it))
            page3.save(flush: true)
        }
        

        // update firstPost
        firstPost.lastActived = new Date() 
        page1.addToPosts(firstPost)
        page1.save(fulsh:true)        
    }

    void testGetFeedByRecentPost() {
        assert 2 == User.count()
        assert 3 == Page.count()
        assert 5 == PageUser.count()
        assert 12 == Post.count()

        def userPostResults = service.getFeedByRecentPost(user)
        def followerPostResults = service.getFeedByRecentPost(follower)
        assert 10 == userPostResults.size()
        assert 8 == followerPostResults.size()

        assert 'first Post' == userPostResults[0].message
        assert "third Post2" == userPostResults[1].message
        assert "page1" == userPostResults[0].page.name

        // test with offset and max
        userPostResults = service.getFeedByRecentPost(user, 1, 9)
        assert 9 == userPostResults.size()
        assert "third Post2" == userPostResults[0].message
        assert "page3" == userPostResults[0].page.name
    }

    void testGetHomeFeedByTopPost() {
        def userPostResults = service.getFeedByTopPost(user)
        def followerPostResults = service.getFeedByTopPost(follower)
        assert 10 == userPostResults.size()
        assert 8 == followerPostResults.size()

        assert "first Post sub3" == userPostResults[0].message
        assert "first Post sub2" == userPostResults[1].message
        assert "page1" == userPostResults[0].page.name
        assert "page3" == userPostResults[9].page.name
        assert "third Post1" == userPostResults[9].message
    }
}
