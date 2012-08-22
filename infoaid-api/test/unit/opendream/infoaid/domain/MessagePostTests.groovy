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

        verifyNotNull(messagePost, 'message')
        
        messagePost.message = ''
        verifyNotBlank(messagePost, 'message')

        messagePost.message = 'Hello World'
        verifyPass(messagePost, 'message')
    }

    void testValidateCreatedBy() {
        mockForConstraintsTests(MessagePost)

        def messagePost = new MessagePost()

        verifyNotNull(messagePost, 'createdBy')

        messagePost.createdBy = 'username'
        verifyPass(messagePost, 'createdBy')
    }

    void testValidateUpdateBy() {
        mockForConstraintsTests(MessagePost)

        def messagePost = new MessagePost()

        verifyNotNull(messagePost, 'updatedBy')

        messagePost.updatedBy = 'username'
        verifyPass(messagePost, 'updatedBy')
    }
}
