package opendream.infoaid.domain

class Page {
    String name
    String lat
    String lng
    Date dateCreated
    Date lastUpdated
    Location location

    static constraints = {
        lat nullable:true
        lng nullable:true
        location nullable:true, unique:false
    }
}
