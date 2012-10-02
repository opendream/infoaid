package opendream.infoaid.controller

import grails.converters.JSON

class FrontPageController {
    def pageService

    def info() {
        def ret = [:]
        def pageNeedActive = pageService.getActiveNeedPage()
        ret.status = 1
        def pages = pageNeedActive.collect {
            [
                name: it.name,
                slug: it.slug,
                lat: it.lat,
                lng: it.lng,
                
                needs: pageService.getLimitNeeds(it.slug, 5).needs.collect {
                   [
                        item: it.item.name,
                        quantity: it.quantity
                   ]
                }
            ]
            
        }
        ret.pages = pages
        ret.totalPages = pages.size()
        
        render ret as JSON
    }
}
