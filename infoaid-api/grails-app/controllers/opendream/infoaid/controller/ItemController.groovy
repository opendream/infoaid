package opendream.infoaid.controller

import opendream.infoaid.domain.Item
import grails.converters.JSON

class ItemController {

    def itemService
    def pageService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def allItems() {
        def items = Item.findAllByStatus(Item.Status.ACTIVE, [sort:"name"])
        items = items.collect { [id: it.id, name: it.name] }
        render items as JSON
    }

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

    def itemHistory() {        
        def ret = [:]
        def result = itemService.getItemHistory(params.user, params.slug, params.itemId, params.since, params.until, params.limit)
        ret.status = 1
        ret.itemHistory = result.itemHistory.collect {
            def item = [
                    id: it.id,
                    class: it.class.name.tokenize('.')[-1],
                    name: it.item.name,
                    message: it.message,
                    picSmall: it.picSmall,
                    picOriginal: it.picOriginal,
                    dateCreated: it.dateCreated.format('yyyy-MM-dd HH:mm'),
                    createdBy: it.createdBy.username,
                    userPicSmall: it.createdBy.picSmall,
                    userPicOriginal: it.createdBy.picOriginal,
                    userId: it.createdBy.id,
                    conversation: it.conversation,
                    lastActived: it.lastActived.time,
                    canDelete: pageService.canDelete(it.createdBy.id, params.user, result.authority.isOwner),
                    comments: it.previewComments.comments.collect {
                        [
                            id: it.id,
                            message: it.message,
                            createdBy: it.user.username,
                            userId: it.user.id,
                            picOriginal: it.user.picOriginal,
                            picLarge: it.user.picLarge,
                            picSmall: it.user.picSmall,
                            lastUpdated: it.lastUpdated.format('yyyy-MM-dd HH:mm'),
                            canDelete: pageService.canDelete(it.user.id, params.user, result.authority.isOwner)
                        ]
                    }
                ]
        }

        ret.total = result.total
        ret.isJoined = result.authority.isJoined
        ret.isOwner = result.authority.isOwner
        render ret as JSON
    }
}
