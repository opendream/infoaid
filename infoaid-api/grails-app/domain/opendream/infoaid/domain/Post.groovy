package opendream.infoaid.domain

class Post {
    Date dateCreated
    Date lastUpdated
    Date lastActived

    static hasMany = [comments:Comment]

    static constraints = {
        lastActived nullable: true
    }
}
