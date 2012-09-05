package opendream.infoaid.controller
import opendream.infoaid.domain.Location
import opendream.infoaid.service.LocationService

import grails.test.mixin.*
import org.junit.*


@TestFor(LocationController)
@Mock([Location, LocationService])
class LocationControllerTests {

    def locationService

    @Before
    void setup() {
        locationService = mockFor(LocationService)
        def location1 = new Location(region: 'location1Region', province: 'location1Province', district: 'location1District',
            subDistrict: 'location1SubDistrict', label: 'location1Label').save(flush: true)
        def location2 = new Location(region: 'location2Region', province: 'location2Province', district: 'location2District',
            subDistrict: 'location2SubDistrict', label: 'location2Label').save(flush: true)
    }

    void testList() {
        locationService.demand.list(1..1) {max -> 
            def params = [:]
            params.max = Math.min(max ?: 10, 100)
            def location = Location.list(params)
        }
        controller.locationService = locationService.createMock()

        controller.list()
        assert response.json['totalLocations'] == 2
        assert response.json['locations'][0].region == 'location1Region'
    }

    void testCreateLocation() {
        locationService.demand.createLocation(1..1) {locationParams ->
            def newLocation = new Location()
            newLocation.properties = locationParams
            if(!newLocation.save()) {
                log.error newLocation.errors
                throw new RuntimeException("${newLocation.errors}")
            }
            newLocation
        }
        controller.locationService = locationService.createMock()


        assert Location.count() == 2     

        params.region = 'location3Region'
        params.province = 'location3Province'
        params.district = 'location3District'
        params.subDistrict = 'location3SubDistrict'
        params.label = 'location3Label'
        controller.createLocation()

        assert Location.count() == 3

        def location = Location.get(3)
        assert response.json.region == 'location3Region'
        assert location.region == 'location3Region'
        assert location.status == Location.Status.ACTIVE
    }

    void testCreateLocationFail() {
        locationService.demand.createLocation(1..1) {region ->
            def newLocation = new Location()
            newLocation.properties = locationParams
            if(!newLocation.save()) {
                log.error newLocation.errors
                throw new RuntimeException("${newLocation.errors}")
            }

            newLocation
        }
        controller.locationService = locationService.createMock()

        params.region = null
        controller.createLocation()
        assert response.json.message == 'can not create new location'
    }

    void testShow() {
        locationService.demand.show(1..1) {locationId ->
            def location = Location.get(locationId)
        }
        controller.locationService = locationService.createMock()

        def location = Location.get(1)

        params.id = location.id
        controller.show()
        assert 'location1Region' == response.json['region']
    }

    void testUpdate() {
        locationService.demand.update(1..4) {locationParams ->
            def location = Location.get(locationParams.id)

            if(!location) {
                log.error location.errors
                throw new RuntimeException("${location.errors}")
                return
            }

            if (locationParams.version != null) {
                if (location.version > locationParams.version) {
                    throw new RuntimeException("${location.errors}")
                    return
                }
            }

            location.properties['region', 'status'] = locationParams

            if(!location.save()) {
                log.error location.errors
                throw new RuntimeException("${location.errors}")
                return
            }

            [id: location.id, region: location.region]
        }
        controller.locationService = locationService.createMock()

        def location = Location.get(1)

        params.id = location.id
        params.version = location.version
        params.region = 'newLocation1Region'

        controller.update()
        location = Location.findById(location.id)
        assert 'newLocation1Region' == location.region

        response.reset()
        params.id = location.id
        params.version = -1
        params.region = 'newNewLocation1Region'
        controller.update()
        assert response.json.message == 'can not edit this location'
        

        params.id = location.id
        params.version = 50
        params.region = 'newNewLocation1Region'
        controller.update()

        location = Location.findById(location.id)
        assert 'newNewLocation1Region' == location.region
    }
    
    void testDisableLocation() {
        locationService.demand.disable(1..1) {locationId ->
            def location = Location.get(locationId)
            if(location) {
                location.status = Location.Status.INACTIVE
            }

            if(!location.save()) {
                log.error location.errors
                throw new RuntimeException("${location.errors}")
            }

            [id: location.id, status: location.status]
        }
        controller.locationService = locationService.createMock()

        def location = Location.get(1)

        params.id = location.id
        controller.disableLocation()

        location = Location.findById(location.id)
        assert location.status == Location.Status.INACTIVE
    }

    void testEnableLocation() {
        locationService.demand.enable(1..1) {locationId ->
            def location = Location.get(locationId)
            if(location) {
                location.status = Location.Status.ACTIVE
            }

            if(!location.save()) {
                log.error location.errors
                throw new RuntimeException("${location.errors}")
            }

            [id: location.id, status: location.status]
        }
        controller.locationService = locationService.createMock()

        def location = Location.get(1)

        params.id = location.id
        controller.enableLocation()

        location = Location.findById(location.id)
        assert location.status == Location.Status.ACTIVE
    }
    
}
