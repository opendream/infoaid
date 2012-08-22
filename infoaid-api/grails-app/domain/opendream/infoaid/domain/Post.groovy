package opendream.infoaid.domain

class Post {
    Date dateCreated
    Date lastUpdated
    Date lastActived
    String createdBy
    String updatedBy

    static hasMany = [comments:Comment]

    static belongsTo = [page:Page]

    static constraints = {
    }
}