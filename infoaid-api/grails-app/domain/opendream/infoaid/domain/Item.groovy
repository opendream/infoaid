package opendream.infoaid.domain

class Item {
    Date dateCreated
    Date lastUpdated
    String name
    Status status

    static hasMany = [needs:Need]

    static constraints = {
        name blank: false, nullable: false, unique: true
        status inList: Status.list()
    }

    public enum Status {
        ACTIVE,
        INACTIVE
        static list() {
            [ACTIVE,INACTIVE]
        }
    }
}