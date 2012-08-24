package opendream.infoaid.controller

import grails.converters.JSON

class PageController {
    def pageService

    def index() { }

    def info() {
        def info = pageService.getInfo(params.pageId)
        def ret = [id: info.id, name: info.name, lat: info.lat, lng: info.lng, dateCreated: info.dateCreated.format('yyyy-MM-dd HH:mm'), 
        lastUpdated: info.lastUpdated.format('yyyy-MM-dd HH:mm')
        ]

        render ret as JSON
    }

    def needs() {
        def needs = pageService.getNeeds()
    }
}
