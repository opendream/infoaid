package opendream.infoaid.controller

import grails.test.mixin.*
import org.junit.*
import opendream.infoaid.domain.Page
import opendream.infoaid.domain.User
import opendream.infoaid.domain.PageUser
import opendream.infoaid.service.PageService
import opendream.infoaid.domain.Post
import opendream.infoaid.domain.Item
import opendream.infoaid.domain.Item.Status
import opendream.infoaid.domain.Need
import opendream.infoaid.domain.MessagePost
import opendream.infoaid.domain.Comment
/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(PageController)
@Mock([Page, PageService, User, PageUser, MessagePost, Post, Item, Need, Comment])
class PageControllerTests {
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

        def user1 = new User(username: "nut", password: "nut", firstname: 'firstname', lastname: 'lastname', dateCreated: date, lastUpdated: date, picOriginal: 'picOri', picLarge: 'picLar', picSmall: 'picSma').save()
        def user2 = new User(username: "nut2", password: "nut2", firstname: 'firstname2', lastname: 'lastname2').save()

        def page1 = new Page(name: "page", lat: "111", lng: "222", dateCreated: date, lastUpdated: date, 
            about: 'this is page 1', picOriginal: 'picOri',picSmall: 'picSma', household: 1, population: 11).save()
        def secondPage = new Page(name: "second-page", lat: "11122", lng: "1234", dateCreated: date, lastUpdated: date, about: 'this is 2nd page').save()
               
        
        def firstPost = new Post(message: 'first post', dateCreated: date, lastUpdated: date, lastActived: date, createdBy: user1, updatedBy: user1)
        def secondPost = new Post(message: 'second post', dateCreated: date, lastUpdated: date, lastActived: date+1, createdBy: user1, updatedBy: user1)
        page1.addToPosts(firstPost)
        page1.save()
        page1.addToPosts(secondPost)
        page1.save()

        def comment = new Comment(message: 'comment1', user: user1)
        def comment2 = new Comment(message: 'comment2', user: user1)
        firstPost.addToComments(comment)
        firstPost.addToComments(comment2)

        def item = new Item(name: 'item', status: Status.ACTIVE).save()
        def firstNeed = new Need(item: item, lastActived: date+2, createdBy: user1, updatedBy: user1, 
            expiredDate: date, message: 'need1', quantity: 9)

        def secondNeed = new Need(item: item,lastActived: date+3, createdBy: user1, updatedBy: user1, 
            expiredDate: date, quantity: 10)
        page1.addToPosts(firstNeed)
        page1.save()
        page1.addToPosts(secondNeed)
        page1.save()

        def fifthPost = new Post(message: 'fifth post', dateCreated: date, lastUpdated: date, lastActived: date+1, createdBy: user1, updatedBy: user1)
        secondPage.addToPosts(fifthPost)
        secondPage.save()

        new PageUser(page: page1, user: user1, relation: PageUser.Relation.OWNER, conversation: 1).save()
        new PageUser(page: page1, user: user2, relation: PageUser.Relation.MEMBER, conversation: 2).save()
        new PageUser(page: secondPage, user: user1, relation: PageUser.Relation.OWNER, conversation: 1).save()
    }

    void testInfo() {
        controller.pageService = new PageService()

        params.slug = 'page-slug'
        
        controller.info()
        assert response.json['picSmall'] == 'picSma'
        assert response.json['name'] == 'page'
        assert response.json['household'] == 1
        assert response.json['population'] == 11
    }

    void testMap() {
        pageService.demand.getInfo(1..1) {slug -> Page.findBySlug(slug)}
        controller.pageService = pageService.createMock()

        params.slug = 'page-slug'
        controller.map()
        def expectResponse = """{"status":1,"id":1,"name":"page","lat":"111","lng":"222","slug":"page-slug"}"""

        assert expectResponse == response.text
    }

    void testMember() {
        controller.pageService = new PageService()

        params.slug = 'page-slug'
        params.offset = 0
        controller.member()

        assert response.json['members'].size() == 2
        assert 'nut2' == response.json['members'][0].username
        assert 1 == response.json.status
    }

    void testTopMember() {
        def page1 = Page.get(1)
        def user3 = new User(username: "nut3", password: "nut3", firstname: 'firstname3', lastname: 'lastname2').save()
        def user4 = new User(username: "nut4", password: "nut4", firstname: 'firstname4', lastname: 'lastname2').save()
        def user5 = new User(username: "nut5", password: "nut5", firstname: 'firstname5', lastname: 'lastname2').save()
        def user6 = new User(username: "nut6", password: "nut6", firstname: 'firstname6', lastname: 'lastname2').save()
        new PageUser(page: page1, user: user3, relation: PageUser.Relation.MEMBER, conversation: 3).save()
        new PageUser(page: page1, user: user4, relation: PageUser.Relation.MEMBER, conversation: 4).save()
        new PageUser(page: page1, user: user5, relation: PageUser.Relation.MEMBER, conversation: 5).save()
        new PageUser(page: page1, user: user6, relation: PageUser.Relation.MEMBER, conversation: 6).save()

        def pageService = new PageService()
        pageService.grailsApplication = [config:[infoaid:[api:[member:[max:5]]]]]
        controller.pageService = pageService

        params.slug = 'page-slug'
        controller.topMember()
        assert response.json['topMembers'].size() == 5
        assert 'nut6' == response.json['topMembers'][0].username
    }

    void testStatus() {
        controller.pageService = new PageService()

        params.slug = 'page-slug'
        controller.status()
        assert 4 == response.json['posts'].size()
        assert 'item 10' == response.json['posts'][0].message
    }

    void testEmptyInfo() {
        controller.pageService = new PageService()

        params.slug = 'abc'
        controller.info()
        assert response.text == '{"status":0}'
    }

    void testNeed() {
        controller.pageService = new PageService()

        params.slug = 'page-slug'
        controller.need()

        assert response.json['needs'][0].message == 'item 9'
        assert response.json['totalNeeds'] == 2
        assert response.json['status'] == 1
    }

    void testLimitNeed() {
        controller.pageService = new PageService()

        params.slug = 'page-slug'
        params.limit = 1
        controller.limitNeed()

        assert response.json['needs'][0].message == 'item 10' // order by dateCreated desc
        assert response.json['needs'].size() == 1
        assert response.json['totalNeeds'] == 2
        assert response.json['status'] == 1
    }

    void testAbout() {
        pageService.demand.getAbout(1..1) {slug -> Page.findBySlug(slug).about}
        controller.pageService = pageService.createMock()

        params.slug = 'page-slug'
        controller.about()

        assert 'this is page 1' == response.json.about
    }

    void testJoinUs() {
        def user3 = new User(username: "nut3", password: "nut3", firstname: 'firstname3', lastname: 'lastname3').save(flush: true)
        def testPage = Page.findBySlug('page-slug')
        assert 2 == testPage.getUsers(0).size()
        
        controller.pageService = new PageService()

        params.userId = 3
        params.slug = 'page-slug'

        controller.joinUs()

        testPage = Page.findBySlug('page-slug')
        assert 3 == testPage.getUsers(0, 10).size()
        assert PageUser.Relation.MEMBER == testPage.getUsers(0, 20).last().relation
        assert 1 == response.json['status']
    }    

    void testTopPost() {
        def postlist = Post.list()
        4.times {
            postlist[it].conversation = it
            postlist[it].save()
        }

        controller.pageService = new PageService()

        params.slug = 'page-slug'
        controller.topPost()
        
        assert 4 == response.json['posts'].size()
        assert 'item 10' == response.json['posts'][0].message
        assert 'item 9' == response.json['posts'][1].message
        assert 'first post' == response.json['posts'][3].message
    }

    void testRecentPost() {
        def postlist = Post.list()
        4.times {
            postlist[it].conversation = it
            postlist[it].save()
        }

        controller.pageService = new PageService()

        params.slug = 'page-slug'
        params.until = '2012-09-12 15:07'
        controller.recentPost()
        
        assert null == response.json['posts']

        response.reset()
        params.slug = 'page-slug'
        params.since = '2012-09-12 15:07'
        params.until = '2020-09-12 15:07'
        controller.recentPost()
        
        assert 4 == response.json['posts'].size()
        assert 'item 10' == response.json['posts'][0].message
        assert 'item 9' == response.json['posts'][1].message
        assert 'first post' == response.json['posts'][3].message
    }

    void testCreatePage() {
        assert 2 == Page.count()

        pageService.demand.createPage(1..1) { userId, name, lat, lng, location, household, population, about, picOriginal -> 
            def page = new Page(name: name, lat: lat, lng: lng, location: location,
            household: household, population: population, about: about, picOriginal: picOriginal).save()
            def user = User.get(userId)
            new PageUser(user: user, page: page, relation: PageUser.Relation.OWNER).save()
            page
        }
        controller.pageService = pageService.createMock()

        params.userId = 1
        params.name = 'my page'
        params.lat = 'my lat'
        params.lng = 'my lng'
        params.location = null
        params.household = 100
        params.population = 300
        params.about = 'about body'
        params.picOriginal = 'picOri'
        controller.createPage()

        assert 3 == Page.count()
        assert 4 == PageUser.count()

        assert 1 == response.json.status
        assert 1 == response.json.userId
        assert 'my page' == response.json.name
        assert 'my lat' == response.json.lat
        assert 'my lng' == response.json.lng
        assert 100 == response.json.household
        assert 300 == response.json.population
        assert 'about body' == response.json.about
        assert 'picOri' == response.json.picOriginal
    }

    void testLeavePage() {
        assert 3 == PageUser.count()

        pageService.demand.leavePage(1..1) { userId, slug -> 
            def user = User.get(userId)
            def page = Page.findBySlug(slug)
            def pageUser = PageUser.findByPageAndUser(page, user)
            pageUser.delete()
        }
        controller.pageService = pageService.createMock()

        params.userId = 1
        params.slug = 'page-slug'
        controller.leavePage()

        assert 2 == PageUser.count()

        assert 1 == response.json.status
        assert "user id: 1 left from page: page-slug" == response.json.message
    }

    void testRemoveUserFromPage() {
        assert 3 == PageUser.count()

        controller.pageService = new PageService()

        params.userId = 1
        params.slug = 'page-slug'
        controller.removeUserFromPage()

        assert 2 == PageUser.count()

        assert 1 == response.json.status
    }

    void testSetRelation() {
        controller.pageService = new PageService()

        params.userId = 1
        params.slug = 'page-slug'
        params.relation = 'Member'

        controller.setRelation()

        def pageUser = PageUser.get(1)

        assert pageUser.relation == PageUser.Relation.MEMBER
    }

    void testPostComment() {
        def thisPost = Post.get(1)
        assert thisPost.conversation == 0
        assert 2 == Comment.count()
        pageService.demand.postComment(1..1) { userId, postId, message -> 
            def user = User.get(userId)
            def commentDate = new Date()
            def post = Post.get(postId)
            
            def comment = new Comment(message: message, dateCreated: commentDate)
            post.addToComments(comment)
            post.lastActived = commentDate
            post.conversation++

            if(!post.save(flush:true)) {
                return false
            }

            def pageUser = PageUser.findByPageAndUser(post.page, user)
            pageUser.conversation++
            pageUser.save()
            [user: user, post: post, comment: comment]
        }

        controller.pageService = pageService.createMock()
        
        params.message = 'this is my comment'
        params.postId = 1
        params.userId = 1
        controller.postComment()
        def user = User.get(1)
        def post = Post.get(1)
        def pageUser = PageUser.findByPageAndUser(post.page, user)

        thisPost = Post.get(1)
        assert pageUser.conversation == 2
        assert thisPost.conversation == 1
        assert 3 == Comment.count()        
    }   

    void testDisableComment() {
        def nut = User.findByUsername("nut")
        //def post = Post.findByMessage('first post')
        def comment = Comment.findByMessage('comment1')

        controller.pageService = new PageService()
        controller.springSecurityService  = [principal:[id:nut.id]]
        params.commentId = comment.id        

        controller.disableComment()

        assert 1 == response.json.status
        assert "comment ${comment.id} is deleted" == response.json.message
        assert false == comment.enabled
    } 

    void testDisableCommentFail() {
        def comment = Comment.findByMessage('comment1')

        controller.pageService = new PageService()
        controller.springSecurityService  = [principal:[id:0]]
        params.commentId = comment.id        

        controller.disableComment()

        assert 0 == response.json.status
        assert "unauthorized user or not found comment" == response.json.message
        assert true == comment.enabled
    }

    void testUpdatePage() {
        def page = Page.findBySlug("page-slug")

        params.slug = 'page-slug'
        params.version = page.version
        params.name = 'newNamePage1'
        params.picOriginal = 'newPicOri'
        controller.updatePage()

        assert page.name == 'newNamePage1'
        assert page.picOriginal == 'newPicOri'

        params.version = page.version - 1
        params.name = 'newNewNamePage1'
        controller.updatePage()
        page = Page.findBySlug("page-slug")
        assert page.name == 'newNamePage1'
    }

    void testDisablePageEnablePage() {
        def page = Page.findBySlug('page-slug')
        assert page.status == Page.Status.ACTIVE
        params.slug = 'page-slug'

        controller.disablePage()
        page = Page.findBySlug('page-slug')

        assert 1 == response.json.status
        assert 'page-slug' == response.json.page.slug
        assert 'INACTIVE' == response.json.page.status

        response.reset()
        params.slug = 'page-slug'
        controller.enablePage()
        page = Page.findBySlug('page-slug')

        assert 1 == response.json.status
        assert 'page-slug' == response.json.page.slug
        assert 'ACTIVE' == response.json.page.status
    }

    void testPostMessage() {
        // param nut , page-slug
        controller.pageService = new PageService()
        def user = User.findByUsername('nut')
        def page = Page.findBySlug('page-slug')

        params.userId = user.id
        params.slug = page.slug
        params.message = 'hello world'

        // controller
        controller.postMessage()

        // expect
        assert 1 == response.json.status
        assert "user: ${user.username} posted message in page: ${page.name}" == response.json.message
        assert 'hello world' == response.json.post.message        
    }

    void testDisablePost() {
        // param nut , postId
        controller.pageService = new PageService()
        def user = User.findByUsername('nut')
        def post = Post.findByMessage('first post')

        params.userId = user.id
        params.postId = post.id
        
        // controller
        controller.disablePost()

        // expect
        assert 1 == response.json.status
        assert post.id == response.json.id
        assert "post ${post.id} is deleted" == response.json.message
        assert Post.Status.INACTIVE == post.status
    }

    void testDisablePostFail() {
        // param nut , postId
        controller.pageService = new PageService()
        def post = Post.findByMessage('first post')

        params.userId = 0
        params.postId = post.id
        
        // controller
        controller.disablePost()

        // expect
        assert 0 == response.json.status
        assert "unauthorized user or not found post" == response.json.message
        assert Post.Status.ACTIVE == post.status
    }

    void testPostNeed() {
        // param nut , page-slug
        controller.pageService = new PageService()
        def user = User.findByUsername('nut')
        def page = Page.findBySlug('page-slug')
        def item = new Item(name: 'water').save(flush: true)

        params.userId = user.id
        params.slug = page.slug
        params.itemId = item.id
        params.quantity = 20

        // controller
        controller.postNeed()

        // expect        
        assert 1 == response.json.status
        assert "user: ${user.username} posted request ${item.name}, quantity: 20 in page: ${page.name}" == response.json.message
    }

    void testSearchPage() {
        def pageService = new PageService()
        pageService.grailsApplication = [config:[infoaid:[api:[search:[max:10]]]]]
        controller.pageService = pageService

        params.word = ''
        controller.searchPage()

        assert response.json['status'] == 1

        assert response.json['totalResults'] == 2

        response.reset()

        params.word = 'sec'
        controller.searchPage()
        assert response.json['pages'][0].name == 'second-page'

        assert response.json['pages'][0].needs.size() == 0

        assert response.json['status'] == 1

        assert response.json['totalResults'] == 1

    }

    void testIsOwner() {
        controller.pageService = new PageService()

        params.slug = 'page-slug'
        def user1 = User.findByUsername('nut')
        params.userId = user1.id

        def result = controller.isOwner()
        assert response.json['isOwner'] == true

        response.reset()

        def user2 = User.findByUsername('nut2')
        params.userId = user2.id
        params.slug = 'page-slug'

        result = controller.isOwner()
        assert response.json['isOwner'] == false
    }

    void testIsJoined() {
        controller.pageService = new PageService()

        params.slug = 'page-slug'
        def user1 = User.findByUsername('nut')
        params.userId = user1.id

        def result = controller.isJoined()
        assert response.json['isJoined'] == true

        response.reset()

        def user2 = User.findByUsername('nut2')
        params.slug = 'page-slug'
        params.userId = user2.id

        result = controller.isJoined()
        assert response.json['isJoined'] == true

        response.reset()

        params.slug = 'page-slug'
        params.userId = 12345633

        result = controller.isJoined()
        assert response.json['isJoined'] == false
    }
}