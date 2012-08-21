package opendream.infoaid.domain



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Post)
class PostTests extends DomainTestTemplate {

    def requiredProperties() {
        ['dateCreated', 'lastUpdated']
    }

    def domainClass() {
        Post.class
    }
}
