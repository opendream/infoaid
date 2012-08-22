package opendream.infoaid.domain



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Post)
class PostTests extends DomainTestTemplate {

    def requiredProperties() {
        ['dateCreated', 'lastUpdated', 'lastActived', 'createdBy', 'updatedBy']
    }

    def domainClass() {
        Post.class
    }

    void testValidateLastActived() {
        mockForConstraintsTests(Post)

        def post = new Post()

        verifyNotNull(post, 'lastActived')

        post.lastActived = new Date()
        verifyPass(post, 'lastActived')
    }
}
