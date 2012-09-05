package opendream.infoaid.controller

import grails.converters.JSON

class FrontPageController {
    def pageService

    def info() {
        def ret = [:]
        def pages = pageService.getSummaryInfo()
        ret.status = 1
        ret.pages = pages.collect {
            [
                name: it.name,
                lat: it.lat,
                lng: it.lng,
                needs: pageService.getLimitNeeds(it.slug, 5).needs.collect {
                    [
                        message: it.message,
                        quantity: it.quantity
                    ]
                }

            ]
        }
        ret.totalPages = pages.size()
        
        render ret as JSON
    }
}
