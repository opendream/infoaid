package opendream.infoaid.domain


import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Page)
class PageTests extends DomainTestTemplate {

    def requiredProperties() {
        ['name', 'lat', 'lng', 'dateCreated', 'lastUpdated', 'location']
    }

    def domainClass() {
        Page.class
    }

    void testValidateName() {
        mockForConstraintsTests(Page)

        def page = new Page()

        verifyNotNull(page, 'name')

        page.name = 'pageName'
        verifyPass(page, 'name')
    }

    void testValidateLat() {
        mockForConstraintsTests(Page)

        def page = new Page()

        page.lat = '128.09988'
        verifyPass(page, 'lat')
    }

    void testValidateLng() {
        mockForConstraintsTests(Page)

        def page = new Page()

        page.lng = '128.09988'
        verifyPass(page, 'lng')
    }

    void testValidateLocation() {
        mockForConstraintsTests(Page)
        mockForConstraintsTests(Location)

        def page = new Page()

        verifyPass(page, 'location')
    }
    
}
