package opendream.infoaid.domain



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Comment)
class CommentTests extends DomainTestTemplate {

    def requiredProperties() {
        ['message', 'dateCreated', 'lastUpdated', 'user']
    }

    def domainClass() {
        Comment.class
    }

    void testValidateMessage() {
        mockForConstraintsTests(Comment)

        def comment = new Comment()

        verifyNotNull(comment, 'message')

        comment.message = ''
        verifyNotBlank(comment, 'message')

        comment.message = 'commentMessage'
        verifyPass(comment, 'message')
    }
}
