package opendream.infoaid.service

import static org.junit.Assert.*
import org.junit.*

import opendream.infoaid.domain.*
import opendream.infoaid.domain.PageUser.Relation

class PageServiceIntegrationTests {
    def pageService
    def date
    def number = 0

    @Before
    void setup() {
        date = new Date()-19
        def date2 = new Date()-20

        def user1 = new User(username: "nut", password: "nut", firstname: 'firstname', 
            lastname: 'lastname', dateCreated: date, lastUpdated: date, 
            picSmall: 'picSma', picOriginal: 'picOri', picLar: 'picLar').save()
        def user2 = new User(username: "nut2", password: "nut2", firstname: 'firstname2', 
            lastname: 'lastname2').save()
        
        def page = new Page(name: "page1", lat: "page1", lng: "page1", 
            dateCreated: date, lastUpdated: date, lastActived: date)
        
        def page2 = new Page(name: "page2", lat: "page2", lng: "page2", 
            dateCreated: date, lastUpdated: date, lastActived: date)
        
        def post = new Post(message: 'post1',dateCreated: date, lastUpdated: date, 
            lastActived: date, createdBy: user1, updatedBy: user1)

        def post2 = new Post(message: 'post2', dateCreated: date, lastUpdated: date, 
            lastActived: date2, createdBy: user1, updatedBy: user1)
        page.addToPosts(post)
        page.addToPosts(post2)
        //page.save()

        20.times {
            def post3 = new Post(message: 'post3: '+it, dateCreated: date, lastUpdated: date, 
                lastActived: date2+it, createdBy: user1, updatedBy: user2)
            page.addToPosts(post3)            
        }
        page.save()
        page2.save()
    
        PageUser.createPage(user1, page, Relation.OWNER)
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testGetPosts() {
        def page = Page.findByName("page1")
        def posts = pageService.getPosts(page.slug, null, null, null, null) // default recent post
        def user = User.findByUsername('nut')
        assert posts.size() == 10
        assert posts.getAt(0).createdBy == user
        assert posts.getAt(0).message == 'post3: 19'
    }

    @Test
    void testGetTopPost() {
        def post = Post.findByMessage('post1')
        def user = User.findByUsername("nut")
        pageService.postComment(user.id, post.id, "my comment for top posts")
                
        def page = Page.findByName("page1")
        def posts = pageService.getTopPost(page.slug, null, null, null, null)
        
        assert posts.size() == 10
        assert posts[0].message == 'post1'
    }

    @Test
    void testGetRecentPost() {
        def page = Page.findByName("page1")
        def posts = pageService.getRecentPost(page.slug, null, null, new Date()-1, null)
        def user = User.findByUsername('nut')
        assert posts.size() == 10
        assert posts[0].createdBy == user
    }

    @Test
    void testGetComments() {
        def page = Page.findByName("page1")
        def posts = pageService.getPosts(page.slug, 0, null, null, null)
        def firstResultPost = posts[0]
        def user = User.findByUsername("nut")

        pageService.postComment(user.id, firstResultPost.id, "my comment11")
        
        10.times {
            pageService.postComment(user.id, firstResultPost.id, "my comment"+it)
        }

        def comment11 = Comment.findByMessage('my comment11')

        def resultsComment = pageService.getComments(firstResultPost.id, null, null, null, null)

        assert resultsComment.totalComments == 11
        assert resultsComment.comments[0].message == 'my comment11'
        assert resultsComment.comments[0].user.picSmall == 'picSma'
        assert resultsComment.comments[0].dateCreated.time == comment11.dateCreated.time


        resultsComment = pageService.getComments(firstResultPost.id, comment11.id+8 as Long, null, null, null)
        assert resultsComment.comments.size() == 3

        resultsComment = pageService.getComments(firstResultPost.id, null, comment11.id+8 as Long, null, null)
        assert resultsComment.comments.size() == 9

        resultsComment = pageService.getComments(firstResultPost.id, null, null, new Date(), null)
        assert resultsComment.comments.size() == 0

        resultsComment = pageService.getComments(firstResultPost.id, null, null, null, new Date())
        assert resultsComment.comments.size() == 11

        resultsComment = pageService.getComments(firstResultPost.id, comment11.id+8 as Long, null, null, new Date())
        assert resultsComment.comments.size() == 3
    }

    @Test
    void testPostComment() {
        def message = "abcdefg"
        def page = Page.findByName("page1")
        def posts = pageService.getPosts(page.slug, 0, null, null, null)
        def post = posts.getAt(0)
        def previousActived = post.lastActived
        def user = User.findByUsername("nut")
        pageService.postComment(user.id, post.id, message)

        def updatedPost = Post.get(post.id)
        assert updatedPost.lastActived > previousActived
        assert updatedPost.conversation == 1

        def newComment = pageService.getComments(updatedPost.id, null, null, null, null).comments.last().message
        assert newComment == message
    }

    @Test
    void testDisableComment() {
        def nut = User.findByUsername("nut")
        def post = Post.findByMessage('post1')
                
        pageService.postComment(nut.id, post.id, 'first comment')
        pageService.postComment(nut.id, post.id, 'second comment')
        assert 2 == post.comments.size()
        assert 2 == post.conversation

        def firstComment = Comment.findByMessage('first comment')
        def result = pageService.disableComment(nut.id, firstComment.id)
        assert 2 == post.comments.size()
        assert 1 == post.conversation
        assert false == firstComment.enabled
        assert 1 == result.status
        assert "comment ${firstComment.id} is deleted" == result.message

        def secondComment = Comment.findByMessage('second comment')
        result = pageService.disableComment(0, secondComment.id)
        assert 2 == post.comments.size()
        assert 1 == post.conversation
        assert true == secondComment.enabled
        assert 0 == result.status
        assert "unauthorized user or not found comment" == result.message
    }

    @Test
    void testPostMessage() {
        def page = Page.findByName("page1")
        def message = "need some help!"
        def user = User.findByUsername("nut")
        def picOriginal = 'picOri'
        def picSmall = 'picSma'
        def result = pageService.createMessagePost(user.id, page.slug, message, picOriginal, picSmall)

        assert 'nut' == result.user.username
        assert page.name == result.page.name
        assert 'need some help!' == result.post.message
        assert 'picOri' == result.post.picOriginal
        assert 'picSma' == result.post.picSmall
    }

    @Test
    void testDisablePost() {
        def post = Post.findByMessage("post1")
        def user = User.findByUsername("nut")
        def result = pageService.disablePost(user.id, post.id)

        assert Post.Status.INACTIVE == post.status
        assert 1 == result.status
        assert post.id == result.id
        assert "post ${post.id} is deleted" == result.message
    }

    @Test
    void testDisablePostFail() {
        def post = Post.findByMessage("post1")
        def result = pageService.disablePost(0, post.id)

        assert Post.Status.ACTIVE == post.status
        assert 0 == result.status
        assert "unauthorized user or not found post" == result.message
    }

    @Test
    void testGetLimitComments() {
        def page = Page.findByName("page1")
        def posts = pageService.getPosts(page.slug, 0, null, null, null)
        def firstResultPost = posts.getAt(0)
        def user = User.findByUsername("nut")

        10.times {
            pageService.postComment(user.id, firstResultPost.id, "my comment"+it)
        }

        def resultsLimitComment = pageService.getLimitComments(firstResultPost.id)

        assert resultsLimitComment.totalComments == 10

        assert resultsLimitComment.comments.last().message == "my comment7"
    }
}
