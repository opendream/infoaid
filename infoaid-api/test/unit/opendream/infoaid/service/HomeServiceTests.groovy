package opendream.infoaid.service

import opendream.infoaid.domain.Comment
import opendream.infoaid.domain.Page
import opendream.infoaid.domain.PageUser
import opendream.infoaid.domain.PageUser.Relation
import opendream.infoaid.domain.Post
import opendream.infoaid.domain.Users

import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(HomeService)
@Mock([Page, Post, PageUser, Users])
class HomeServiceTests {
    def user
    def follower    

    @Before
    void setUp() {
        Page.metaClass.generateSlug = {-> 'slug'}
        Page.metaClass.isDirty = {name -> false}
        def date = new Date() - 1

        // mock user
        user = new Users(username: 'admin', password: 'password', 
            firstname: 'thawatchai', lastname: 'jong')
        user.save(flush:true)

        follower = new Users(username: 'follower', password: 'password', 
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
        def firstPost = new Post(dateCreated: date, lastUpdated: date, 
            lastActived: new Date(), createdBy: 'nut 1 post', updatedBy: 'boy')
        page1.addToPosts(firstPost)
        4.times {
            page1.addToPosts(new Post(dateCreated: date, lastUpdated: date, 
            lastActived: new Date(), createdBy: "nut $it", updatedBy: 'boy'))
        }
        page1.save()

        4.times {
            page2.addToPosts(new Post(dateCreated: date, lastUpdated: date, 
            lastActived: new Date(), createdBy: 'nut', updatedBy: 'boy'))
        }
        page2.save()

        3.times {
            page3.addToPosts(new Post(dateCreated: date, lastUpdated: date, 
            lastActived: new Date(), createdBy: "nut3 $it", updatedBy: 'boy'))
        }
        page3.save()   

        // update firstPost
        firstPost.lastActived = new Date() 
        page1.addToPosts(firstPost)
        page1.save(fulsh:true)        
    }

    void testGetFeedByLastActived() {
        assert 2 == Users.count()
        assert 3 == Page.count()
        assert 5 == PageUser.count()
        assert 12 == Post.count()

        def userPostResults = service.getFeedByLastActived(user)
        def followerPostResults = service.getFeedByLastActived(follower)
        assert 10 == userPostResults.size()
        assert 8 == followerPostResults.size()

        assert "nut 1 post" == userPostResults[0].createdBy
        assert "nut3 2" == userPostResults[1].createdBy
        assert "page1" == userPostResults[0].page.name

        // test with offset and max
        userPostResults = service.getFeedByLastActived(user, 1, 9)
        assert 9 == userPostResults.size()
        assert "nut3 2" == userPostResults[0].createdBy
        assert "page3" == userPostResults[0].page.name
    }
}
