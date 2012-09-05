package opendream.infoaid.controller

import opendream.infoaid.domain.Comment
import opendream.infoaid.domain.Page
import opendream.infoaid.domain.User
import opendream.infoaid.service.PageService
import opendream.infoaid.domain.Post

import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(PostController)
@Mock([Comment, Page, User, Post, PageService])
class PostControllerTests {

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
        pageService = mockFor(PageService)

        def user1 = new User(username: "nut", password: "nut", firstname: 'firstname', lastname: 'lastname', dateCreated: date, lastUpdated: date).save()
        def user2 = new User(username: "nut2", password: "nut2", firstname: 'firstname2', lastname: 'lastname2').save()

        def page1 = new Page(name: "page", lat: "111", lng: "222", dateCreated: date, lastUpdated: date, about: 'this is page 1').save()
        def secondPage = new Page(name: "second-page", lat: "11122", lng: "1234", dateCreated: date, lastUpdated: date, about: 'this is 2nd page').save()
               
        
        def firstPost = new Post(message: 'first post', dateCreated: date, lastUpdated: date, lastActived: date, createdBy: 'nut', updatedBy: 'boy')
        def secondPost = new Post(message: 'second post', dateCreated: date, lastUpdated: date, lastActived: date+1, createdBy: 'nut', updatedBy: 'boy')
        page1.addToPosts(firstPost)
        page1.addToPosts(secondPost)

        def comment = new Comment(message: 'comment1', user: user1)
        def comment2 = new Comment(message: 'comment2', user: user1)
        def comment3 = new Comment(message: 'comment3', user: user1)
        def comment4 = new Comment(message: 'comment4', user: user1)
        def comment5 = new Comment(message: 'comment5', user: user1)
        firstPost.addToComments(comment)
        firstPost.addToComments(comment2)
        firstPost.addToComments(comment3)
        firstPost.addToComments(comment4)
        firstPost.addToComments(comment5)

        page1.save()
    }

    void testComment() {
        pageService.demand.getComments(1..1) {postId, fromId=null, toId=null, since=null, until=null -> 
            def max = 50
            def comments = Comment.createCriteria().list(max: max) {
                post {
                    idEq(postId)
                }
                if(fromId) {
                    ge('id', fromId)
                }
                if(toId) {
                    le('id', toId)
                }
                if(since) {
                    ge('dateCreated', since)
                }
                if(until) {
                    le('dateCreated', until)
                }
                order('dateCreated', 'asc')
            }

            [comments: comments, totalComments: comments.totalCount]
        }
        controller.pageService = pageService.createMock()

        def post1 = Post.findByMessage('first post')

        params.postId = post1.id

        controller.comment()
        assert response.json['comments'][0].message == 'comment1'
        assert response.json['totalComments'] == 5
    }

    void testPreviewComment() {
        pageService.demand.getLimitComments(1..1) {postId -> 
            def comments = Post.findById(postId).previewComments
        }
        controller.pageService = pageService.createMock()

        def post1 = Post.findByMessage('first post')

        params.postId = post1.id

        controller.previewComment()
        assert response.json['comments'][0].message == 'comment1'
        assert response.json['comments'].size() == 3
        assert response.json['totalComments'] == 5
    }
}