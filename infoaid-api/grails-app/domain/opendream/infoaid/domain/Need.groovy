package opendream.infoaid.domain

class Need extends Post {

    Date expiredDate
    Integer previousSumQuantity = 0
    Integer quantity = 0
    Item item

    static constraints = {
        quantity nullable: false
        previousSumQuantity nullable: false
        item nullable: false
    }

    def beforeInsert() {
        this.message = "${item.name} $quantity"
    }
}
