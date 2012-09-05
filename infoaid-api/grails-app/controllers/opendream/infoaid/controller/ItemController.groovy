package opendream.infoaid.controller

import opendream.infoaid.domain.Item
import grails.converters.JSON

class ItemController {

    def itemService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list(Integer max) {
        def items = itemService.list(max)
        def ret = [:]
        ret.items = items.collect {
            [
                id: it.id,
                name: it.name,
                lastUpdated: it.lastUpdated,
                dateCreated: it.dateCreated,
                status: it.status.toString(),
                needs: it.needs.collect {
                    [
                        id: it.id,
                        message: it.message,
                        quantity: it.quantity
                    ]
                }
            ]
        }
        ret.totalItems = items.size()
        render ret as JSON
    }

    def createItem() {
        def ret = [:]
        try {
            def newItem = itemService.createItem(params.name)
            ret.id = newItem.id
            ret.name = newItem.name
            ret.lastUpdated = newItem.lastUpdated
            ret.dateCreated = newItem.dateCreated
            ret.needs = newItem.needs
            ret.status = newItem.status.toString()

            render ret as JSON
        } catch(e) {
            ret = [message: 'can not create new item', item: params]
            render ret as JSON
        }
        
    }

    def show(Long id) {
        def item = itemService.show(id)
        if (!item) {
            def errorMessage = [message: "Item not Found"]
            render errorMessage as JSON
            return
        }

        def ret = [:]
        ret.id = item.id
        ret.name = item.name
        ret.lastUpdated = item.lastUpdated
        ret.dateCreated = item.dateCreated
        ret.needs = item.needs.collect{
            [
                id: it.id,
                message: it.message,
                quantity: it.quantity
            ]
        }
        ret.status = item.status.toString()

        render ret as JSON
    }

    def update() {
        def ret = [:]
        try {
            def item = itemService.update(params)
            ret.id = item.id
            ret.name = item.name
            ret.lastUpdated = item.lastUpdated
            ret.dateCreated = item.dateCreated
            ret.needs = item.needs
            ret.status = item.status.toString()
            render ret as JSON
        } catch (e) {
            ret = [message: 'can not edit this item', item: params]
            render ret as JSON
        }
    }
    
    def disableItem() {
        def ret = [:]
        try {
            def item = itemService.disable(params.id)
            ret.id = item.id
            ret.name = item.name
            ret.lastUpdated = item.lastUpdated
            ret.dateCreated = item.dateCreated
            ret.needs = item.needs
            ret.status = item.status.toString()
            render ret as JSON
        } catch (e) {
            ret = [message: 'can not disable this item', item: params]
            render ret as JSON
        }
    }

    def enableItem() {
        def ret = [:]
        try {
            def item = itemService.enable(params.id)
            ret.id = item.id
            ret.name = item.name
            ret.lastUpdated = item.lastUpdated
            ret.dateCreated = item.dateCreated
            ret.needs = item.needs
            ret.status = item.status.toString()
            render ret as JSON
        } catch (e) {
            ret = [message: 'can not enable this item', item: params]
            render ret as JSON
        }
    }
}
