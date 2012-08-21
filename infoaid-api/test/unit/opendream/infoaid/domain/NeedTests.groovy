package opendream.infoaid.domain



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Need)
class NeedTests extends DomainTestTemplate {

    def requiredProperties() {
        ['expiredDate', 'quantity']
    }

    def domainClass() {
        Need.class
    }

    void testValidateQuantity() {
        mockForConstraintsTests(Need)

        def need = new Need()

        verifyPass(need, 'quantity')
    }
    
}
