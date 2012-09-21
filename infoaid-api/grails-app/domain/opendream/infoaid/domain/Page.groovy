package opendream.infoaid.domain

import org.codehaus.groovy.grails.commons.ConfigurationHolder

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
    String picSmall
    String picOriginal
    String picLarge

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
        picSmall nullable: true
        picOriginal nullable: true
        picLarge nullable: true
    }

    public enum Status {
        ACTIVE,
        INACTIVE
        static list() {
            [ACTIVE,INACTIVE]
        }
    }

    def getUsers(offset=0, max=ConfigurationHolder.config.infoaid.api.allmember.limited) {
        PageUser.findAllByPage(this,[max: max, sort: 'conversation', order: 'desc', offset: offset]).collect { [user: it.user, relation: it.relation] }
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
