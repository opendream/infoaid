package opendream.infoaid.controller

import grails.converters.JSON

class PostController {

    def pageService

    def index() { }

    def comment() {
        def ret = [:]
        def since
        def until
        if(params.since) {
            since = new Date().parse("yyyy-MM-dd H:m", params.since)
        }
        if(params.until) {
            until = new Date().parse("yyyy-MM-dd H:m", params.until)
        }
        if(params.postId) {
            def comments = pageService.getComments(params.user, params.long('postId'), params.fromId, params.toId, since, until)
            ret.comments = comments.comments.collect{
                [
                    id: it.id,
                    message: it.message,
                    userId: it.user.id,
                    user: it.user.username,
                    createdBy: it.user.username,
                    picSmall: it.user.picSmall,
                    picOriginal: it.user.picOriginal,
                    picLarge: it.user.picLarge,
                    dateCreated: it.dateCreated.format('yyyy-MM-dd HH:mm'),
                    lastUpdated: it.lastUpdated.format('yyyy-MM-dd HH:mm'),
                    canDelete: pageService.canDelete(it.user.id, params.user, comments.author.isOwner)                    
                ]
            }
            ret.totalComments = comments.totalComments
            
        }
        render ret as JSON
    }

    def previewComment() {
        def ret = [:]
        if(params.postId) {
            def comments = pageService.getLimitComments(params.postId)
            ret.comments = comments.comments.collect{
                [
                    id: it.id,
                    message: it.message,
                    userId: it.user.id,
                    user: it.user.username,
                    picSmall: it.user.picSmall,
                    picOriginal: it.user.picOriginal,
                    picLarge: it.user.picLarge,
                    dateCreated: it.dateCreated.format('yyyy-MM-dd HH:mm'),
                    lastUpdated: it.lastUpdated.format('yyyy-MM-dd HH:mm'),
                    canDelete: it.user.id == params.user?.id ?true:false
                ]
            } 
            ret.totalComments = comments.totalComments

        }
        render ret as JSON
    }
}
