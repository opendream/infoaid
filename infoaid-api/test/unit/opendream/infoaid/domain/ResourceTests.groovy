package opendream.infoaid.domain



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Resource)
class ResourceTests extends DomainTestTemplate {

    def requiredProperties() {
        ['expiredDate', 'quantity', 'item']
    }

    def domainClass() {
        Resource.class
    }

    void testValidateQuantity() {
        mockForConstraintsTests(Resource)

        def resource = new Resource()

        verifyPass(resource, 'quantity')
    }
}
