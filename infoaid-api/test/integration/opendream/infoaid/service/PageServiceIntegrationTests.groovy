package opendream.infoaid.service

import static org.junit.Assert.*
import org.junit.*

import opendream.infoaid.domain.*

class PageServiceIntegrationTests {
    def pageService
    def date
    def number = 0

    @Before
    void setup() {

        Page.metaClass.generateSlug = {-> delegate.slug = ""+(number++)}
        Page.metaClass.isDirty = {name -> false}
        date = new Date()-19
        def date2 = new Date()-20
        
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
    
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testGetTopPost() {
        def post = Post.findByMessage('post1')
        pageService.postComment(post.id, "my comment for top posts")
                
        def posts = pageService.getTopPost('0', 0)
        
        assert posts.size() == 10
        assert posts[0].message == 'post1'
    }

    @Test
    void testGetRecentPost() {
        def page = Page.findByName("page1")
        assert 22 == Post.count
        def posts = pageService.getRecentPost(page.slug, 0)
        
        assert posts.size() == 10
        assert posts[0].createdBy == 'yo19'
    }
}
