package opendream.infoaid.domain

import grails.test.mixin.*
import org.junit.*

import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass

abstract class DomainTestTemplate {

    def mustTestProperties = true

    def requiredProperties() {

    }

    def domainClass() {

    }

    void testProperties() {
        if (mustTestProperties) {
            def defaultDomainClass = new DefaultGrailsDomainClass(domainClass())

            def domainProperties = defaultDomainClass.persistentProperties*.name
            def missing_properties = requiredProperties() - domainProperties
            assert 0 == missing_properties.size(),
                "Domain class is missing some required properties => ${missing_properties}"
        }
    }

    void verifyNotNull(instance, field) {
        instance.validate([field])
        assertEquals "${field} must fail null validation.",
            "nullable", instance.errors[field]
    }

    void verifyNotBlank(instance, field) {
        instance.validate([field])
        assertEquals "${field} must fail blank validation.",
            "blank", instance.errors[field]
    }

    void verifyUnique(instance, field) {
        instance.validate([field])
        assertEquals "${field} must fail unique validation.",
            "unique", instance.errors[field]
    }

    void verifyPass(instance, field) {
        assertTrue "${field} value = ${instance[field]} must pass all validations.",
            instance.validate([field])
    }

}