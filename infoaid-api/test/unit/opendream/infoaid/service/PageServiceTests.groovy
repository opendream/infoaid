package opendream.infoaid.service



import grails.test.mixin.*
import org.junit.*

import opendream.infoaid.domain.Page
import opendream.infoaid.domain.Post
import opendream.infoaid.domain.Comment
import opendream.infoaid.domain.Location
import opendream.infoaid.domain.PageUser
import opendream.infoaid.domain.Users
import opendream.infoaid.domain.Need
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PageService)
@Mock([Page, Post, Comment, Location, PageUser, Users, Need])
class PageServiceTests {
    def date

    @Before
    void setup() {
        Page.metaClass.generateSlug = {-> 'slug'}
        Page.metaClass.isDirty = {name -> false}
        date = new Date()-19
        def date2 = new Date()-20
        
        def page = new Page(name: "page1", lat: "page1", lng: "page1", dateCreated: date, lastUpdated: date)
        
        def page2 = new Page(name: "page2", lat: "page2", lng: "page2", dateCreated: date, lastUpdated: date)
        
        def post = new Post(dateCreated: date, lastUpdated: date, lastActived: date, createdBy: 'nut', updatedBy: 'boy')
        def post2 = new Post(dateCreated: date, lastUpdated: date, lastActived: date2, createdBy: 'yo', updatedBy: 'boy')
        page.addToPosts(post)
        page.addToPosts(post2)

        20.times {
            def post3 = new Post(dateCreated: date, lastUpdated: date, lastActived: date2+it, createdBy: 'yo'+it, updatedBy: 'boy')
            page.addToPosts(post3)
        }
        page.save()
        page2.save()
    }



    void testGetInfo() {

        def info = service.getInfo(1)
        
        assert info.name == 'page1'
        assert info.lat == 'page1'
        assert info.lng == 'page1'
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

        assert Page.list().size() == 4

        def page = Page.get(3)
        assert page.location == location

        def page2 = Page.get(4)
        assert page2.location == null

        def pageUser = PageUser.get(1)
        assert pageUser.user == user
        assert pageUser.user != user2
        assert pageUser.relation == PageUser.Relation.OWNER

        service.joinPage(user2.id, page.id)
        assert page.users.size() == 2

        service.inactivePage(user.id, page.id)
        def pageStatus = Page.get(page.id).status
        assert pageStatus == Page.Status.INACTIVE

        service.leavePage(user2.id, page.id)
        assert page.users.size() == 1
    }

    void testGetMembers() {
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

        def page = Page.get(3)

        def member = service.getMembers(page.id)
        assert member.size() == 1
        assert member[0].user == user

        service.joinPage(user2.id, page.id)
        member = service.getMembers(page.id)

        assert member.size() == 2
        assert member[0].user == user
        assert member[1].user == user2
    }

    void testGetAllNeeds() {
        def newNeed = new Need(lastActived: date, createdBy: 'nut', updatedBy: 'nut', expiredDate: date, message: 'message', quantity: 10)
        def newNeed2 = new Need(lastActived: date, createdBy: 'nut', updatedBy: 'nut', expiredDate: date, message: 'message', quantity: 10)

        def page = Page.get(1)
        page.addToPosts(newNeed)
        page.addToPosts(newNeed2)
        page.save()
        def results = service.getAllNeeds(page.id)
        assert results.totalNeeds == 2

        page = Page.get(1)
        newNeed2.status = Post.Status.INACTIVE
        page.addToPosts(newNeed2)
        page.save()

        results = service.getAllNeeds(page.id)
        assert results.totalNeeds == 1
    }

    void testGetLimitNeeds() {
        def newNeed = new Need(lastActived: date, createdBy: 'nut', updatedBy: 'nut', expiredDate: date, message: 'need1', quantity: 10)
        def newNeed2 = new Need(lastActived: date, createdBy: 'nut', updatedBy: 'nut', expiredDate: date, message: 'need2', quantity: 10)
        def newNeed3 = new Need(lastActived: date, createdBy: 'nut', updatedBy: 'nut', expiredDate: date, message: 'need3', quantity: 10)

        def page = Page.get(1)
        page.addToPosts(newNeed)
        page.addToPosts(newNeed2)
        page.addToPosts(newNeed3)
        page.save()

        def results = service.getLimitNeeds(page.id, 2)
        assert results.needs.size() == 2
        assert results.totalNeeds == 3
    }

    void testCreateNeed() {
        def page = Page.get(1)
        def message = 'hello new need'

        assert page.posts.size() == 22

        service.createNeed(page.id, message)
        page = Page.get(1)
        assert page.posts.size() == 23

    }

    void testCreateMessagePost() {
        def page = Page.get(1)
        def message = 'hello new message'
    }

}
