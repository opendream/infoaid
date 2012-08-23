package opendream.infoaid.domain

class Item {
    Date dateCreated
    Date lastUpdated
    String name

    static hasMany = [needs:Need]

    static constraints = {
        name blank: false
    }
}