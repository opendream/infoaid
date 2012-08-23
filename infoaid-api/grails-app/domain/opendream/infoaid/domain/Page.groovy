package opendream.infoaid.domain

class Page {
    def slugGeneratorService

    String name
    String lat
    String lng
    Date dateCreated
    Date lastUpdated
    Location location
    String slug = ""

    static hasMany = [posts:Post]
    static transients = ['slugGeneratorService']

    static constraints = {
        lat nullable:true
        lng nullable:true
        location nullable:true, unique:false
    }

    def beforeInsert() {
        this.slug = slugGeneratorService.generateSlug(this.class, "slug", name)
    }

    def beforeUpdate() {
        if (isDirty('name')) {
            this.slug = slugGeneratorService.generateSlug(this.class, "slug", name)
        }
    }
}
