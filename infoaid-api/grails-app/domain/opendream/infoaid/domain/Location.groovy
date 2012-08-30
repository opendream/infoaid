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
    Status status = Status.ACTIVE

    static constraints = {
        lat nullable: true
        lng nullable: true
    }

    public enum Status {
        ACTIVE,
        INACTIVE
        static list() {
            [ACTIVE,INACTIVE]
        }
    }
}
