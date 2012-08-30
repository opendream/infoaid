package opendream.infoaid.controller

//import org.springframework.dao.DataIntegrityViolationException
import opendream.infoaid.domain.Item
import grails.converters.JSON

class ItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def items = Item.list(params)
        def ret = [:]
        ret.items = items.collect {
            [
                name: it.name,
                status: it.status.toString(),
                dateCreated: it.dateCreated.format('yyyy-MM-dd HH:mm')
            ]
        }
        ret.totalItems = items.size()
        render ret as JSON
    }

    def createItem() {
        if(params.name) {
            def item = new Item(params)
            if (!item.save()) {
                def errorMessage = [message: "Can't save this item ${params.name}"]
                log.error(item.errors)
                render errorMessage as JSON
                return
            }
        }
    }

    def show(Long id) {
        def item = Item.get(id)
        if (!item) {
            def errorMessage = [message: "Item not Found"]
            render errorMessage as JSON
            return
        }

        def ret = [:]
        ret.name = item.name
        ret.status = item.status.toString()
        ret.dateCreated = item.dateCreated.format('yyyy-MM-dd HH:mm')
        render ret as JSON
    }

    def update(Long id, Long version) {        
        def itemInstance = Item.get(id)
        if (!itemInstance) {
            return
        }

        if (version != null) {
            if (itemInstance.version > version) {
                itemInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'item.label', default: 'Item')] as Object[],
                          "Another user has updated this Item while you were editing")
                return
            }
        }

        itemInstance.properties = params

        if (!itemInstance.save(flush: true)) {
            def errorMessage = [message: "Can't update this item ${itemInstance.name}"]
            render errorMessage as JSON
            return
        }
    }

    def disableItem(long id) {
        def item = Item.get(id)
        if(!item) {
            def errorMessage = [message: "Item not found"]
            render errorMessage as JSON
            return
        }

        item.status = Item.Status.INACTIVE
        if(!item.save()) {
            def errorMessage = [message: "Can't disable this item ${item.name}"]
            log.error(item.errors)
            render errorMessage as JSON
            return
        }
    }
}
