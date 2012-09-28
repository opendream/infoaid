package opendream.infoaid.domain

class Resource extends Need {

    def beforeInsert() {
        this.message = "${item.name} $quantity"
    }
}
