package opendream.infoaid.domain

class Item {
    Date dateCreated
    Date lastUpdated
    String name
    Integer quantity = 0

    static hasMany = [needs:Need]

    static constraints = {
        name blank: false
        quantity nullable: false
    }
}