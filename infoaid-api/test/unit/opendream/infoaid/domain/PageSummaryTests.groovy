package opendream.infoaid.domain

import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(PageSummary)
class PageSummaryTests extends DomainTestTemplate {

    def requiredProperties() {
        ['name', 'lat', 'lng', 'dateCreated', 'lastUpdated', 'slug', 'household', 'population']
    }

    def domainClass() {
        PageSummary.class
    }

    void testValidateName() {
        mockForConstraintsTests(PageSummary)

        def pageSummary = new PageSummary()

        verifyNotNull(pageSummary, 'name')

        pageSummary.name = 'pageName'
        verifyPass(pageSummary, 'name')
    }

    void testValidateLat() {
        mockForConstraintsTests(PageSummary)

        def pageSummary = new PageSummary()

        pageSummary.lat = '128.09988'
        verifyPass(pageSummary, 'lat')
    }

    void testValidateLng() {
        mockForConstraintsTests(PageSummary)

        def pageSummary = new PageSummary()

        pageSummary.lng = '128.09988'
        verifyPass(pageSummary, 'lng')
    }
}
