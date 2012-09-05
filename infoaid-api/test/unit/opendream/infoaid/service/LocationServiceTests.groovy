package opendream.infoaid.service


import opendream.infoaid.domain.Location
import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(LocationService)
@Mock([Location])
class LocationServiceTests {

    @Before
    void setup() {
        def location1 = new Location(region: 'location1Region', province: 'location1Province', district: 'location1District',
            subDistrict: 'location1SubDistrict', label: 'location1Label').save(flush: true)
        def location2 = new Location(region: 'location2Region', province: 'location2Province', district: 'location2District',
            subDistrict: 'location2SubDistrict', label: 'location2Label').save(flush: true)
    }

    void testList() {
        def locationList = service.list()
        assert locationList.size() == 2
        assert locationList.first() == Location.get(1)
    }

    void testCreateLocation() {
    	assert Location.count() == 2
    	def locationParams = [region: 'location3Region', province: 'location3Province', district: 'location3District',
            subDistrict: 'location3SubDistrict', label: 'location3Label']
    	service.createLocation(locationParams)

    	assert Location.count() == 3
    	assert Location.list().last().region == 'location3Region'
    }

    void testCreateLocationFail() {
    	assert Location.count() == 2

    	shouldFail(RuntimeException) {
            service.createLocation('')
        }
    }
    
    void testShow() {
    	def location = service.show(1)
    	assert location == Location.get(1)
    }

    void testUpdate() {
    	def locationParams = [id: 1, region: 'nut', status: Location.Status.INACTIVE]
        service.update(locationParams)

        def location1 = Location.get(1)

        assert location1.status == Location.Status.INACTIVE
        assert location1.region == 'nut'
    }

    void testUpdateFail() {
        def locationParams2 = [id: 1, region: 'abc', version: -1]

        shouldFail(RuntimeException) {
        	service.update(locationParams2)
        }
    }

    void testDisable() {
    	def locationId = 1
    	service.disable(locationId)

    	def location = Location.get(locationId)
    	assert location.status == Location.Status.INACTIVE
    }

    void testEnable() {
    	def locationId = 1
    	service.enable(locationId)

    	def location = Location.get(locationId)
    	assert location.status == Location.Status.ACTIVE
    }
}
