package opendream.infoaid.controller

import opendream.infoaid.domain.Location
import grails.converters.JSON

class LocationController {

    def locationService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list(Integer max) {
        def locations = locationService.list(max)
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
        def ret = [:]
        try {
            def newLocation = locationService.createLocation(params)
            ret.region = newLocation.region
            ret.province = newLocation.province
            ret.district = newLocation.district
            ret.subDistrict = newLocation.subDistrict
            ret.label = newLocation.label
            ret.lat = newLocation.lat
            ret.lng = newLocation.lng
            ret.dateCreated = newLocation.dateCreated
            ret.lastUpdated = newLocation.lastUpdated
            ret.status = newLocation.status.toString()

            render ret as JSON
        } catch(e) {
            ret = [message: 'can not create new location', location: params]
            render ret as JSON
        }
    }

    def show(Long id) {
        def location = locationService.show(id)
        if (!location) {
            def errorMessage = [message: "Location not Found"]
            render errorMessage as JSON
            return
        }

        def ret = [:]
        ret.id = location.id
        ret.region = location.region
        ret.province = location.province
        ret.district = location.district
        ret.subDistrict = location.subDistrict
        ret.label = location.label
        ret.lat = location.lat
        ret.lng = location.lng
        ret.dateCreated = location.dateCreated
        ret.lastUpdated = location.lastUpdated
        ret.status = location.status.toString()

        render ret as JSON
    }

    def update() {        
        def ret = [:]
        try {
            def location = locationService.update(params)
            ret.id = location.id
            ret.region = location.region
            ret.province = location.province
            ret.district = location.district
            ret.subDistrict = location.subDistrict
            ret.label = location.label
            ret.lat = location.lat
            ret.lng = location.lng
            ret.dateCreated = location.dateCreated
            ret.lastUpdated = location.lastUpdated
            ret.status = location.status.toString()
            render ret as JSON
        } catch (e) {
            ret = [message: 'can not edit this location', location: params]
            render ret as JSON
        }
    }
    
    def disableLocation() {
        def ret = [:]
        try {
            def location = locationService.disable(params.id)
            ret.id = location.id
            ret.status = location.status.toString()
            render ret as JSON
        } catch (e) {
            ret = [message: 'can not disable this location', location: params]
            render ret as JSON
        }
    }

    def enableLocation() {
        def ret = [:]
        try {
            def location = locationService.enable(params.id)
            ret.id = location.id
            ret.status = location.status.toString()
            render ret as JSON
        } catch (e) {
            ret = [message: 'can not enable this location', location: params]
            render ret as JSON
        }
    }
    
}
