package opendream.infoaid.service



import grails.test.mixin.*
import org.junit.*

import opendream.infoaid.domain.Page
import opendream.infoaid.domain.Post
import opendream.infoaid.domain.Comment
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PageService)
@Mock([Page,Post,Comment])
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
            page.addToPosts(new Post(dateCreated: date, lastUpdated: date, lastActived: date2+it, createdBy: 'yo'+it, updatedBy: 'boy'))
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

    void testGetPost() {
    	def results = service.getPosts(1, 0)
    	assert results.posts.size() == 10
    	assert results.posts.getAt(0).createdBy == 'yo19'
    }

    void testGetComment() {
        def comment = new Comment(message: "my comment11")
        def resultsPost = service.getPosts(1,0)
        def post = resultsPost.posts.getAt(0)

        post.addToComments(comment)
        post.save()

        10.times {
            service.postComment(post.id, "my comment"+it)
        }

        def comment11 = Comment.findByMessage('my comment11')

        def resultsComment = service.getComments(post.id)

        assert resultsComment.totalComments == 11

        assert resultsComment.comments.getAt(0).dateCreated == comment11.dateCreated

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
}
