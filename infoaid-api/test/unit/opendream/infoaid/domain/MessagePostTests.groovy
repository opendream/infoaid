package opendream.infoaid.domain



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(MessagePost)
class MessagePostTests extends DomainTestTemplate {

    def requiredProperties() {
        ['dateCreated', 'lastUpdated', 'message', 'createdBy', 'updatedBy']
    }

    def domainClass() {
        MessagePost.class
    }

    void testValidateMessage() {
        mockForConstraintsTests(MessagePost)

        def messagePost = new MessagePost()

        verifyPass(messagePost, 'message')
        
        messagePost.message = ''
        verifyNotBlank(messagePost, 'message')

        messagePost.message = 'Hello World'
        verifyPass(messagePost, 'message')
    }
}
