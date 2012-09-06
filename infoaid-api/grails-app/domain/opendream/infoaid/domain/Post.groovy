package opendream.infoaid.domain

class Post {
    String message
    Date dateCreated
    Date lastUpdated
    Date lastActived
    String createdBy
    String updatedBy
    Status status = Status.ACTIVE
    Page page
    Integer conversation = 0

    public enum Status {
        ACTIVE,
        INACTIVE
        static list() {
            [ACTIVE,INACTIVE]
        }
    }

    static hasMany = [comments:Comment]

    static constraints = {
        status inList: Status.list()
        message blank: false, nullable: false
    }

    static mapping = {
        sort lastActived: "desc"
    }
    
    def getPreviewComments() {
        def previewComments = Comment.createCriteria().list(max: 3) {
            eq('post', this)
            order('dateCreated', 'desc')
        }

        [comments: previewComments, totalComments: previewComments.totalCount]
    }
    
}