package opendream.infoaid.service

import opendream.infoaid.domain.Location

class LocationService {

    def list(Integer max) {
    	def params = [:]
    	params.max = Math.min(max ?: 10, 100)
    	def location = Location.list(params)
    }

    def createLocation(locationParams) {
    	def newLocation = new Location()
    	newLocation.properties = locationParams
    	if(!newLocation.save()) {
    		log.error newLocation.errors
            throw new RuntimeException("${newLocation.errors}")
    	}
    	newLocation
    }

    def show(locationId) {
    	def location = Location.get(locationId)
    }

    def update(locationParams) {
    	def location = Location.get(locationParams.id)

    	if(!location) {
    		log.error location.errors
    		throw new RuntimeException("${location.errors}")
    		return
    	}

    	if (locationParams.version != null) {
            if (location.version > locationParams.version) {
                log.error location.errors
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
    	[id: location.id, region: location.region, province: location.province, district: location.district,
    	subDistrict: location.subDistrict, label: location.label, lat: location.lat, lng: location.lng, status: location.status]
    }

    def disable(locationId) {
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

    def enable(locationId) {
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
}
