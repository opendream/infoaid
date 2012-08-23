package opendream.infoaid.service



import grails.test.mixin.*
import org.junit.*

import opendream.infoaid.domain.Page
import opendream.infoaid.domain.Post
import opendream.infoaid.domain.Comment
import opendream.infoaid.domain.Location
import opendream.infoaid.domain.PageUser
import opendream.infoaid.domain.Users
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PageService)
@Mock([Page, Post, Comment, Location, PageUser, Users])
class PageServiceTests {
	def date

	@Before
	void setup() {
		date = new Date()-19
		def date2 = new Date()-20
		
		def page = new Page(name: "page1", lat: "page1", lng: "page1", dateCreated: date, lastUpdated: date)
		def post = new Post(dateCreated: date, lastUpdated: date, lastActived: date, createdBy: 'nut', updatedBy: 'boy')
		def post2 = new Post(dateCreated: date, lastUpdated: date, lastActived: date2, createdBy: 'yo', updatedBy: 'boy')
		page.addToPosts(post)
		page.addToPosts(post2)

        20.times {
            def post3 = new Post(dateCreated: date, lastUpdated: date, lastActived: date2+it, createdBy: 'yo'+it, updatedBy: 'boy')
            page.addToPosts(post3)
        }
		page.save()
	}

    void testGetInfo() {

    	def info = service.getInfo(1)
    	
    	assert info.name == 'page1'
    	assert info.lat == 'page1'
    	assert info.lng == 'page1'
    	assert info.dateCreated == date
    	assert info.lastUpdated == date
    }

    void testGetPosts() {
    	def results = service.getPosts(1, 0)
    	assert results.posts.size() == 10
    	assert results.posts.getAt(0).createdBy == 'yo19'


    }

    void testGetComments() {
        def comment = new Comment(message: "my comment11")
        def resultsPost = service.getPosts(1,0)
        def firstResultPost = resultsPost.posts.getAt(0)

        service.postComment(firstResultPost.id, "my comment11")
        
        10.times {
            service.postComment(firstResultPost.id, "my comment"+it)
        }

        def comment11 = Comment.findByMessage('my comment11')

        def resultsComment = service.getComments(firstResultPost.id)

        assert resultsComment.totalComments == 11

        assert resultsComment.comments.getAt(0).dateCreated == comment11.dateCreated

    }

    void testGetLimitComments() {
        def resultsPost = service.getPosts(1,0)
        def firstResultPost = resultsPost.posts.getAt(0)

        10.times {
            service.postComment(firstResultPost.id, "my comment"+it)
        }

        def resultsLimitComment = service.getLimitComments(firstResultPost.id)

        assert resultsLimitComment.totalComments == 10

        assert resultsLimitComment.comments.last().message == "my comment2"
    }

    void testPostComment() {
        def message = "abcdefg"
        def resultsPost = service.getPosts(1,0)
        def post = resultsPost.posts.getAt(0)
        def previousActived = post.lastActived
        service.postComment(post.id, message)

        def updatedPost = Post.get(post.id)
        assert updatedPost.lastActived > previousActived

        def newComment = service.getComments(updatedPost.id).comments.last().message
        assert newComment == message
    }

    void testCreatePageJoinPageLeavePageInactivePage() {
        def user = new Users(username: 'admin', password: 'password', firstname: 'thawatchai', lastname: 'jong')
        user.save()
        def user2 = new Users(username: 'admin2', password: 'password2', firstname: 'jong', lastname: 'thawatchai')
        user2.save()

        def name1 = 'testCreatePage1'
        def lat1 = 'lat1'
        def lng1 = 'lng1'
        def location = new Location(region: 'region1', province: 'province1', district: 'subDistrict1', label: 'label1')

        def name2 = 'testCreatePage2'
        def lat2 = 'lat2'
        def lng2 = 'lng2'

        service.createPage(user.id, name1, lat1, lng1, location)
        service.createPage(user.id, name2, lat2, lng2, null)
        service.createPage(user2.id, name2, lat2, lng2, null)

        assert Page.list().size() == 3

        def page = Page.get(2)
        assert page.location == location

        def page2 = Page.get(3)
        assert page2.location == null

        def pageUser = PageUser.get(1)
        assert pageUser.user == user
        assert pageUser.user != user2
        assert pageUser.relation == PageUser.Relation.OWNER

        service.joinPage(user2.id, page.id)
        assert page.users.size() == 2

        service.inactivePage(user.id, page.id)
        println page.users.relation
        def pageStatus = Page.get(page.id).status
        assert pageStatus == Page.Status.INACTIVE

        service.leavePage(user2.id, page.id)
        assert page.users.size() == 1


    }


}
