package opendream.infoaid.domain

class Need extends Post {
    Date expiredDate
    Integer quantity = 0

    static constraints = {
        quantity nullable: false
    }
}
