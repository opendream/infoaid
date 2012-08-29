package opendream.infoaid.domain

class Page {
    def slugGeneratorService

    String name
    String lat
    String lng
    Date dateCreated
    Date lastUpdated
    Location location
    Status status = Status.ACTIVE
    String slug = ""
    Integer household
    Integer population
    String about

    static hasMany = [posts:Post]
    static transients = ['slugGeneratorService']

    static constraints = {
        lat nullable:true
        lng nullable:true
        location nullable:true, unique:false
        name unique:true, nullable: false
        status inList: Status.list()
        slug unique: true
        household nullable:true
        population nullable:true
        about maxSize: 500, nullable: true
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

    def beforeInsert() {
        generateSlug()
    }

    def beforeUpdate() {
        if (isDirty('name')) {
            generateSlug()
        }
    }

    protected void generateSlug() {
        this.slug = slugGeneratorService.generateSlug(this.class, "slug", name)
    }
}
