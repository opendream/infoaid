package opendream.infoaid.domain

class Page {
    String name
    String lat
    String lng
    Date dateCreated
    Date lastUpdated
    Location location
    Status status = Status.ACTIVE

    static hasMany = [posts:Post]

    static constraints = {
        lat nullable:true
        lng nullable:true
        location nullable:true, unique:false
        name unique:true
        status inList: Status.list()
    }

    public enum Status {
        ACTIVE,
        INACTIVE
        static list() {
            [ACTIVE,INACTIVE]
        }
    }

    def getUsers() {
        PageUser.findAllByPage(this).collect { [user: it.user, relation: it.relation] }
    }
}
