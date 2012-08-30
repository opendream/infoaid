package opendream.infoaid.controller
import opendream.infoaid.domain.Location

import grails.test.mixin.*
import org.junit.*


@TestFor(LocationController)
@Mock(Location)
class LocationControllerTests {

    @Before
    void setup() {
        def location1 = new Location(region: 'location1Region', province: 'location1Province', district: 'location1District',
            subDistrict: 'location1SubDistrict', label: 'location1Label').save(flush: true)
        def location2 = new Location(region: 'location2Region', province: 'location2Province', district: 'location2District',
            subDistrict: 'location2SubDistrict', label: 'location2Label').save(flush: true)
    }

    void testList() {

        controller.list()
        assert response.json['totalLocations'] == 2
        assert response.json['locations'][0].region == 'location1Region'
    }

    void testCreateLocation() {
        assert Location.count() == 2

        params.region = 'location3Region'
        params.province = 'location3Province'
        params.district = 'location3District'
        params.subDistrict = 'location3SubDistrict'
        params.label = 'location3Label'
        controller.createLocation()
        assert Location.count() == 3
    }

    void testShow() {
        def location = Location.get(1)

        params.id = location.id

        controller.show()
        assert 'location1Region' == response.json['region']

    }

    void testUpdate() {
        def location = Location.get(1)

        params.id = location.id
        params.version = location.version
        params.region = 'newLocation1Region'

        controller.update()
        location = Location.findById(location.id)
        assert 'newLocation1Region' == location.region

        params.id = location.id
        params.version = -1
        params.region = 'newNewLocation1Region'
        controller.update()

        location = Location.findById(location.id)
        assert 'newLocation1Region' == location.region
    }
    
    void testDisableLocation() {
        def location = Location.get(1)

        params.id = location.id
        controller.disableLocation()

        location = Location.findById(location.id)
        assert location.status == Location.Status.INACTIVE
    }
    
}
