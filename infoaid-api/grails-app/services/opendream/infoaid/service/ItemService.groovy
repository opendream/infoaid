package opendream.infoaid.service
import opendream.infoaid.domain.Item
import opendream.infoaid.domain.Need
import opendream.infoaid.domain.Resource

class ItemService {
    def grailsApplication
    def pageService

    def list(Integer max) {
    	def params = [:]
    	params.max = Math.min(max ?: 10, 100)
    	def item = Item.list(params)
    }

    def createItem(name) {
    	def newItem = new Item(name: name)
    	if(!newItem.save()) {
    		log.error newItem.errors
            throw new RuntimeException("${newItem.errors}")
    	}
    	newItem
    }

    def show(itemId) {
    	def item = Item.get(itemId)
    }

    def update(itemParams) {
    	def item = Item.get(itemParams.id)

    	if(!item) {
    		log.error item.errors
    		throw new RuntimeException("${item.errors}")
    		return
    	}

    	if (itemParams.version != null) {
            if (item.version > itemParams.version) {
                log.error item.errors
	    		throw new RuntimeException("${item.errors}")
	    		return
            }
        }

        item.properties['name', 'status'] = itemParams

    	if(!item.save()) {
    		log.error item.errors
    		throw new RuntimeException("${item.errors}")
    		return
    	}
    	[id: item.id, name: item.name]
    }

    def disable(itemId) {
    	def item = Item.get(itemId)

    	if(item) {
    		item.status = Item.Status.INACTIVE
    	}

    	if(!item.save()) {
    		log.error item.errors
    		throw new RuntimeException("${item.errors}")
    	}
    	[id: item.id, status: item.status]
    }

    def enable(itemId) {
    	def item = Item.get(itemId)

    	if(item) {
    		item.status = Item.Status.ACTIVE
    	}

    	if(!item.save()) {
    		log.error item.errors
    		throw new RuntimeException("${item.errors}")
    	}
    	[id: item.id, status: item.status]
    }

    def getItemHistory(user, slug, itemId, fromId=null, toId=null, since, until, limit) {
        limit = limit?:grailsApplication.config.infoaid.api.post.max
        def list = Need.createCriteria().list() {
            page {
                eq('slug', slug)
            }
            maxResults(limit)
            if(itemId) {
                item {
                    eq('id', itemId)
                }
            } 
            if(fromId) {
                ge('id', fromId)
            }
            if(toId) {
                le('id', toId)
            }           
            if(since) {
                ge('dateCreated', since)
            }
            if(until) {
                le('dateCreated', until)
            }
            order('lastActived', 'desc')
        }
        def authority = pageService.getPageAuthority(user, slug)
        //need:list.findAll{it.class==Need}, resources:list.findAll{it.class==Resource}
        return [itemHistory: list, total:list.size(), authority: authority]
    }
}
