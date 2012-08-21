package opendream.infoaid.domain

class Location {
    String region
    String province
    String district
    String subDistrict
    String label
    String lat
    String lng
    Date dateCreated
    Date lastUpdated

    static constraints = {
        lat nullable: true
        lng nullable: true
    }
}
