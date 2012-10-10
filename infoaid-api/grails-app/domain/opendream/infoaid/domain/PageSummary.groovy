package opendream.infoaid.domain

class PageSummary {
    static mapWith = "mongo"
    Long pageId
    String name
    String lat
    String lng
    Date dateCreated
    Date lastUpdated
    String slug = ""
    Integer household
    Integer population 
    Boolean hasNeed = false  
    List items = []

    static constraints = {
        pageId unique:true, nullable: false
        name unique:true, nullable: false
        slug unique: true
        lat nullable:true
        lng nullable:true
        household nullable:true
        population nullable:true
        items nullable:true
    }

    def beforeInsert() {
        this.hasNeed = checkNeed()
    }

    def beforeUpdate() {
        this.hasNeed = checkNeed()
    }

    protected Boolean checkNeed() {
        if(items) {
            items.each {
                if(it['need'] && it.need > 0) {
                    return true
                }
            }
        } else {
            return false
        }
    }
}
