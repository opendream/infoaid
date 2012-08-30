package opendream.infoaid.controller

import opendream.infoaid.domain.Location
import grails.converters.JSON

class LocationController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def locations = Location.list(params)
        def ret = [:]
        ret.locations = locations.collect {
            [
                region: it.region,
                province: it.province,
                district: it.district,
                subDistrict: it.subDistrict,
                label: it.label,
                lat: it.lat,
                lng: it.lng,
                dateCreated: it.dateCreated.format('yyyy-MM-dd HH:mm'),
                lastUpdated: it.lastUpdated.format('yyyy-MM-dd HH:mm'),
                status: it.status.toString()
            ]
        }
        ret.totalLocations = locations.size()
        render ret as JSON
    }

    def createLocation() {
        def location = new Location(params)
        if (!location.save()) {
            def errorMessage = [message: "Can't save this location"]
            log.error(location.errors)
            render errorMessage as JSON
            return
        }
    }

    def show(Long id) {
        def location = Location.get(id)
        if (!location) {
            def errorMessage = [message: "Location not Found"]
            render errorMessage as JSON
            return
        }

        def ret = [:]
        ret.region = location.region
        ret.province = location.province
        ret.district = location.district
        ret.subDistrict = location.subDistrict
        ret.label = location.label
        ret.lat = location.lat
        ret.lng = location.lng
        ret.dateCreated = location.dateCreated.format('yyyy-MM-dd HH:mm')
        ret.lastUpdated = location.lastUpdated.format('yyyy-MM-dd HH:mm')
        ret.status = location.status.toString()

        render ret as JSON
    }

    def update(Long id, Long version) {        
        def locationInstance = Location.get(id)
        if (!locationInstance) {
            return
        }

        if (version != null) {
            if (locationInstance.version > version) {
                locationInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'location.label', default: 'Location')] as Object[],
                          "Another user has updated this Location while you were editing")
                return
            }
        }

        locationInstance.properties = params

        if (!locationInstance.save(flush: true)) {
            def errorMessage = "Can't update this location"
            render errorMessage as JSON
            return
        }
    }
    
    def disableLocation(long id) {
        def location = Location.get(id)
        if(!location) {
            def errorMessage = "Location not found"
            render errorMessage as JSON
            return
        }

        location.status = Location.Status.INACTIVE
        if(!location.save()) {
            def errorMessage = "Can't disable this location"
            log.error(location.errors)
            render errorMessage as JSON
            return
        }
    }
    
}
