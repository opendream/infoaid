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
            lastname: 'lastname', dateCreated: date, lastUpdated: date).save()
        def user2 = new User(username: "nut2", password: "nut2", firstname: 'firstname2', 
            lastname: 'lastname2').save()
        
        def page = new Page(name: "page1", lat: "page1", lng: "page1", 
            dateCreated: date, lastUpdated: date, lastActived: date)
        
        def page2 = new Page(name: "page2", lat: "page2", lng: "page2", 
            dateCreated: date, lastUpdated: date, lastActived: date)
        
        def post = new Post(message: 'post1',dateCreated: date, lastUpdated: date, 
            lastActived: date, createdBy: 'nut', updatedBy: 'boy')

        def post2 = new Post(message: 'post2', dateCreated: date, lastUpdated: date, 
            lastActived: date2, createdBy: 'yo', updatedBy: 'boy')
        page.addToPosts(post)
        page.addToPosts(post2)
        //page.save()

        20.times {
            def post3 = new Post(message: 'post3: '+it, dateCreated: date, lastUpdated: date, 
                lastActived: date2+it, createdBy: 'yo'+it, updatedBy: 'boy')
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
        def posts = pageService.getPosts(page.slug, 0, 10) // default recent post
        assert posts.size() == 10
        assert posts.getAt(0).createdBy == 'yo19'
    }

    @Test
    void testGetTopPost() {
        def post = Post.findByMessage('post1')
        def user = User.findByUsername("nut")
        pageService.postComment(user.id, post.id, "my comment for top posts")
                
        def page = Page.findByName("page1")
        def posts = pageService.getTopPost(page.slug, 0)
        
        assert posts.size() == 10
        assert posts[0].message == 'post1'
    }

    @Test
    void testGetRecentPost() {
        def page = Page.findByName("page1")
        def posts = pageService.getRecentPost(page.slug, 0)
        
        assert posts.size() == 10
        assert posts[0].createdBy == 'yo19'
    }

    @Test
    void testGetComments() {
        def page = Page.findByName("page1")
        def posts = pageService.getPosts(page.slug, 0, 10)
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
        assert resultsComment.comments[0].dateCreated.time == comment11.dateCreated.time


        resultsComment = pageService.getComments(firstResultPost.id, 10 as Long, null, null, null)
        assert resultsComment.totalComments == 3 // comment id start at 2

        resultsComment = pageService.getComments(firstResultPost.id, null, 10 as Long, null, null)
        assert resultsComment.totalComments == 9 // comment id start at 2

        resultsComment = pageService.getComments(firstResultPost.id, null, null, new Date(), null)
        assert resultsComment.totalComments == 0 // comment id start at 2

        resultsComment = pageService.getComments(firstResultPost.id, null, null, null, new Date())
        assert resultsComment.totalComments == 11 // comment id start at 2

        resultsComment = pageService.getComments(firstResultPost.id, 10 as Long, null, null, new Date())
        assert resultsComment.totalComments == 3 // comment id start at 2
    }

    @Test
    void testPostComment() {
        def message = "abcdefg"
        def page = Page.findByName("page1")
        def posts = pageService.getPosts(page.slug, 0, 10)
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
    void testGetLimitComments() {
        def page = Page.findByName("page1")
        def posts = pageService.getPosts(page.slug, 0, 10)
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
