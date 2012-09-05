package opendream.infoaid.domain

class Comment {

    String message
    Date dateCreated
    Date lastUpdated
    User user

    static belongsTo = [post:Post]

    static constraints = {
        message blank: false, nullable: false
    }
}