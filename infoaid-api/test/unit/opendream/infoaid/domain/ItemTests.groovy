package opendream.infoaid.domain



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Item)
class ItemTests extends DomainTestTemplate {

    def requiredProperties() {
        ['dateCreated', 'lastUpdated', 'name', 'quantity']
    }

    def domainClass() {
        Item.class
    }

    void testValidateName() {
        mockForConstraintsTests(Item)

        def item = new Item()

        verifyNotNull(item, 'name')

        item.name = ''
        verifyNotBlank(item, 'name')

        item.name = 'itemName'
        verifyPass(item, 'name')
    }

    void testValidateQuantity() {
        mockForConstraintsTests(Item)

        def item = new Item()
        
        verifyPass(item, 'quantity')
    }
}
