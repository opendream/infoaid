package opendream.infoaid.domain



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Location)
class LocationTests extends DomainTestTemplate {

    def requiredProperties() {
        ['lng','lat','label','subDistrict','district','province','region','dateCreated', 'lastUpdated']
    }

    def domainClass() {
        Location.class
    }

    void testValidateLabel() {
        mockForConstraintsTests(Location)

        def location = new Location()

        verifyNotNull(location, 'label')

        location.label = 'locationLabel'
        verifyPass(location, 'label')
    }

    void testValidateSubDistrict() {
        mockForConstraintsTests(Location)

        def location = new Location()

        verifyNotNull(location, 'subDistrict')

        location.subDistrict = 'locationSubDistrict'
        verifyPass(location, 'subDistrict')
    }

    void testValidateDistrict() {
        mockForConstraintsTests(Location)

        def location = new Location()

        verifyNotNull(location, 'district')

        location.district = 'locationDistrict'
        verifyPass(location, 'district')
    }

    void testValidateProvince() {
        mockForConstraintsTests(Location)

        def location = new Location()

        verifyNotNull(location, 'province')

        location.province = 'locationProvince'
        verifyPass(location, 'province')
    }

    void testValidateRegion() {
        mockForConstraintsTests(Location)

        def location = new Location()

        verifyNotNull(location, 'region')

        location.region = 'locationRegion'
        verifyPass(location, 'region')
    }
}
