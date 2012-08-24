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

    void testInfo() {
        def date = new Date()
        def dateStr = date.toString()
        Page.metaClass.generateSlug = {-> 'slug'}
        Page.metaClass.isDirty = {name -> false}

        new Page(id: 1, name: "page", lat: "111", lng: "222", dateCreated: date, lastUpdated: date).save()
        pageService = mockFor(PageService)

        pageService.demand.getInfo(1..2) {id -> Page.get(id)}

        controller.pageService = pageService.createMock()
        params.pageId = 1
        def dateFormat = "yyyy-MM-dd HH:mm"
        def expectResponse = """{"id":1,"name":"page","lat":"111","lng":"222","dateCreated":"${date.format(dateFormat)}","lastUpdated":"${date.format(dateFormat)}"}"""
        controller.info()

        assert expectResponse == response.text

        params.pageId = 2
        controller.info()
        assert response.text == null
    }

    
}
