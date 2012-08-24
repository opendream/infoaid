package opendream.infoaid.domain

class Post {
    Date dateCreated
    Date lastUpdated
    Date lastActived
    String createdBy
    String updatedBy
    Status status = Status.ACTIVE

    public enum Status {
        ACTIVE,
        INACTIVE
        static list() {
            [ACTIVE,INACTIVE]
        }
    }

    static hasMany = [comments:Comment]

    static belongsTo = [page:Page]

    static constraints = {
        status inList: Status.list()
    }
    
    def getPreviewComments() {
        def previewComments = Comment.createCriteria().list(max: 3) {
            eq('post', this)
        }

        return previewComments
    }
    
}