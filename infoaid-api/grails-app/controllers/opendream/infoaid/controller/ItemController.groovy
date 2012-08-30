package opendream.infoaid.controller

//import org.springframework.dao.DataIntegrityViolationException
import opendream.infoaid.domain.Item
import grails.converters.JSON

class ItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [itemInstanceList: Item.list(params), itemInstanceTotal: Item.count()]
    }

    def createItem() {
        if(params.name) {
            def item = new Item(params)
            if (!item.save()) {
                def errorMessage = [message: "Can't save this item ${params.name}"]
                log.error(item.errors)
                render errorMessage as JSON
            }
        }
    }

    def show(Long id) {
        def itemInstance = Item.get(id)
        if (!itemInstance) {
            def errorMessage = "Item not Found"
            render errorMessage as JSON
            return
        }

        [itemInstance: itemInstance]
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
            def errorMessage = "Can't update this item ${itemInstance.name}"
            render errorMessage as JSON
            return
        }
    }

    def disableItem(long id) {
        def item = Item.get(id)
        if(!item) {
            def errorMessage = "Item not found"
            render errorMessage as JSON
            return
        }

        item.status = Item.Status.INACTIVE
        if(!item.save()) {
            def errorMessage = "Can't disable this item ${item.name}"
            log.error(item.errors)
            render errorMessage as JSON
            return
        }
    }
}
