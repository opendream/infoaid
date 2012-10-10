package opendream.infoaid.controller

import opendream.infoaid.domain.PageSummary
import grails.converters.JSON

class FrontPageController {
    def pageService

    def info() {
        def ret = [:]
        def pageSummary = PageSummary.findAllByHasNeed(true)
        //def pageNeedActive = pageService.getActiveNeedPage()
        ret.status = 1
        def pages = pageSummary.collect {
            [
                name: it.name,
                slug: it.slug,
                lat: it.lat,
                lng: it.lng,                
                //needs: pageService.getLimitNeeds(it.slug, 5).needs.collect {
                needs: it.items.collect {
                    [
                        item: it.name,
                        quantity: it.need
                    ]
                }
            ]
            
        }
        
        ret.pages = pages
        ret.totalPages = pages.size()
        
        render ret as JSON
    }
}
