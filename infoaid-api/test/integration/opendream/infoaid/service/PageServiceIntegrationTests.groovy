package opendream.infoaid.service

import static org.junit.Assert.*
import org.junit.*

import opendream.infoaid.domain.*
import opendream.infoaid.domain.PageUser.Relation

class PageServiceIntegrationTests {
    def pageService
    def fixtureLoader
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
        PageSummary.list().each {
            it.delete(flush:true)
        }
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
        def result = pageService.getTopPost(user, page.slug, null, null, null, null)
        
        assert result.posts.size() == 10
        assert result.posts[0].message == 'post1'
        assert result.author.isJoined == true
        assert result.author.isOwner == true
    }

    @Test
    void testGetRecentPost() {
        def page = Page.findByName("page1")
        def user = User.findByUsername('nut')

        def result = pageService.getRecentPost(user, page.slug, null, null, new Date()-1, null)        
        assert result.posts.size() == 10
        assert result.posts[0].createdBy == user
        assert result.author.isJoined == true
        assert result.author.isOwner == true
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

        def resultsComment = pageService.getComments(user, firstResultPost.id, null, null, null, null)

        assert resultsComment.totalComments == 11
        assert resultsComment.comments[0].message == 'my comment11'
        assert resultsComment.comments[0].user.picSmall == 'picSma'
        assert resultsComment.comments[0].dateCreated.time == comment11.dateCreated.time


        resultsComment = pageService.getComments(user, firstResultPost.id, comment11.id+8 as Long, null, null, null)
        assert resultsComment.comments.size() == 3

        resultsComment = pageService.getComments(user, firstResultPost.id, null, comment11.id+8 as Long, null, null)
        assert resultsComment.comments.size() == 9

        resultsComment = pageService.getComments(user, firstResultPost.id, null, null, new Date(), null)
        assert resultsComment.comments.size() == 0

        resultsComment = pageService.getComments(user, firstResultPost.id, null, null, null, new Date())
        assert resultsComment.comments.size() == 11

        resultsComment = pageService.getComments(user, firstResultPost.id, comment11.id+8 as Long, null, null, new Date())
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

        def newComment = pageService.getComments(user, updatedPost.id, null, null, null, null).comments.last().message
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
        def picLarge = 'picLarge'
        def picSmall = 'picSma'
        def result = pageService.createMessagePost(user.id, page.slug, message, picOriginal, picLarge, picSmall)

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

    @Test
    void testGetPageAllItemSummary() {
        def fixture = fixtureLoader.load("pages")
        fixture.load {
            food(Item,
                name: "Food",
            )

            water(Item,
                    name: "Water",
                )

            man(Item,
                    name: "Man",
                )

            def date = new Date()

            need1(Need,
                    message: "food 1",
                    page: bangkok,
                    item: food,
                    quantity: 10,
                    dateCreated: date,
                    lastUpdated: date,
                    lastActived: date,
                    createdBy: noom,
                    updatedBy: noom,
                    expiredDate: date + 100,
                    status: Post.Status.ACTIVE
                )

            need2(Need,
                    message: "food 2",
                    page: bangkok,
                    item: food,
                    quantity: 4,
                    dateCreated: date,
                    lastUpdated: date,
                    lastActived: date,
                    createdBy: noom,
                    updatedBy: noom,
                    expiredDate: date + 100,
                    status: Post.Status.ACTIVE
                )

            need3(Need,
                    message: "water 1",
                    page: bangkok,
                    item: water,
                    quantity: 100,
                    dateCreated: date,
                    lastUpdated: date,
                    lastActived: date,
                    createdBy: noom,
                    updatedBy: noom,
                    expiredDate: date + 100,
                    status: Post.Status.ACTIVE
                )

            resource1(Resource,
                    message: "give food 8",
                    page: bangkok,
                    item: food,
                    quantity: 8,
                    dateCreated: date,
                    lastUpdated: date,
                    lastActived: date,
                    createdBy: noom,
                    updatedBy: noom,
                    expiredDate: date + 100,
                    status: Post.Status.ACTIVE
                )
        }
        def summary = pageService.getPageAllItemSummary(fixture.bangkok)        

        assert summary.size() == 2

        def food = summary.find { it.name == "Food" }
        assert food.need == 14
        assert food.resource == 8

        def water = summary.find { it.name == "Water" }
        assert water.need == 100
        assert water.resource == 0

        pageService.createOrUpdatePageSummary(fixture.bangkok)
        def pageSummary = pageService.addPageSummaryItems(fixture.bangkok, summary)
        assert 2 == pageSummary.items.size()
        food = pageSummary.items.find {it.name == 'Food'}
        assert 14 == food.need
        assert 8 == food.resource
        water = pageSummary.items.find {it.name == 'Water'}
        assert 100 == water.need
        assert 0 == water.resource
    }
}
