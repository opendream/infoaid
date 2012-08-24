package opendream.infoaid.controller



import grails.test.mixin.*
import org.junit.*
import opendream.infoaid.domain.Page
import opendream.infoaid.service.PageService

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(PageController)
@Mock([Page, PageService])
class PageControllerTests {
    def pageService
    def date

    @Before
    void setup() {
        date = new Date()
        Page.metaClass.generateSlug = {-> 'slug'}
        Page.metaClass.isDirty = {name -> false}

        new Page(name: "page", lat: "111", lng: "222", dateCreated: date, lastUpdated: date).save()
        pageService = mockFor(PageService)

        pageService.demand.getInfo(1..1) {id -> Page.get(id)}

        controller.pageService = pageService.createMock()
    }

    void testInfo() {
        
        params.pageId = 1
        def dateFormat = "yyyy-MM-dd HH:mm"
        def expectResponse = """{"id":1,"name":"page","lat":"111","lng":"222","dateCreated":"${date.format(dateFormat)}","lastUpdated":"${date.format(dateFormat)}"}"""
        controller.info()

        assert expectResponse == response.text
    }

    void testEmptyInfo() {
        params.pageId = 2
        controller.info()
        assert response.text == '{}'
    }
}
