package opendream.infoaid.service

import opendream.infoaid.domain.Comment
import opendream.infoaid.domain.Page
import opendream.infoaid.domain.PageUser
import opendream.infoaid.domain.PageUser.Relation
import opendream.infoaid.domain.Post
import opendream.infoaid.domain.User

import static org.junit.Assert.*
import org.junit.*

class HomeServiceTests {
    def homeService        

    @Before
    void setUp() {
        def date = new Date() - 1

        // mock user
        def user = new User(username: 'adminx', password: 'password', 
            firstname: 'thawatchai', lastname: 'jong')
        user.save(failOnError: true, flush:true)
        //def user = User.findByUsername("admin")
        def follower = new User(username: 'follower', password: 'password', 
            firstname: 'nut', lastname: 'tong')
        follower.save(failOnError: true, flush:true)

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
            lastActived: new Date(), createdBy: user, updatedBy: user, conversation:0)
        page1.addToPosts(firstPost)
        page1.save(flush: true)
        4.times {
            page1.addToPosts(new Post(message:"first Post sub$it", dateCreated: date, lastUpdated: date, 
            lastActived: new Date(), createdBy: follower, updatedBy: user, conversation:20+it))
            page1.save(flush: true)
        }
        

        4.times {
            page2.addToPosts(new Post(message:"second Post$it", dateCreated: date, lastUpdated: date, 
            lastActived: new Date(), createdBy: follower, updatedBy: user, conversation:10+it))
            page2.save(flush: true)
        }
        

        3.times {
            page3.addToPosts(new Post(message:"third Post$it", dateCreated: date, lastUpdated: date, 
            lastActived: new Date(), createdBy: follower, updatedBy: user, conversation:1+it))
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
        def user = User.findByUsername("adminx")
        def follower = User.findByUsername("follower")

        def userPostResults = homeService.getFeedByRecentPost(user)
        def followerPostResults = homeService.getFeedByRecentPost(follower)
        assert 10 == userPostResults.size()
        assert 8 == followerPostResults.size()

        assert 'first Post' == userPostResults[0].message
        assert "third Post2" == userPostResults[1].message
        assert "page1" == userPostResults[0].page.name

        // test with offset and max
        userPostResults = homeService.getFeedByRecentPost(user, 1, 9)
        assert 9 == userPostResults.size()
        assert "third Post2" == userPostResults[0].message
        assert "page3" == userPostResults[0].page.name
    }

    void testGetHomeFeedByTopPost() {
        def user = User.findByUsername("adminx")
        def follower = User.findByUsername("follower")

        def userPostResults = homeService.getFeedByTopPost(user)
        def followerPostResults = homeService.getFeedByTopPost(follower)
        assert 10 == userPostResults.size()
        assert 8 == followerPostResults.size()

        assert "first Post sub3" == userPostResults[0].message
        assert "first Post sub2" == userPostResults[1].message
        assert "page1" == userPostResults[0].page.name
        assert "page3" == userPostResults[9].page.name
        assert "third Post1" == userPostResults[9].message
    }
}
