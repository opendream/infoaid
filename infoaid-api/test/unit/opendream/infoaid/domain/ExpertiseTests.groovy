package opendream.infoaid.domain


import grails.test.mixin.*
import org.junit.*

@TestFor(Expertise)
class ExpertiseTests extends DomainTestTemplate {

	def requiredProperties() {
        ['name']
    }

    def domainClass() {
        Expertise.class
    }

    void testValidateName() {
        mockForConstraintsTests(Expertise)

        def expertise = new Expertise()
        verifyNotNull(expertise, 'name')
        expertise.name = 'Programming'
        expertise.save()
        verifyPass(expertise, 'name')

        def expertise2 = new Expertise()
        expertise2.name = 'Programming'
        verifyUnique(expertise2, 'name')
        expertise2.name = 'Cooking'
        verifyPass(expertise2, 'name')
    }

    void testValidateDescription() {
    	mockForConstraintsTests(Expertise)

    	def expertise = new Expertise()
    	verifyPass(expertise, 'description')
    }
}