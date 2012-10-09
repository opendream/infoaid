package opendream.infoaid.domain

class Post {
    String message
    Date dateCreated
    Date lastUpdated
    Date lastActived
    User createdBy
    User updatedBy
    Status status = Status.ACTIVE
    Page page
    Integer conversation = 0
    String picOriginal
    String picLarge
    String picSmall

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
        picOriginal nullable: true
        picLarge nullable: true
        picSmall nullable: true
    }

    static mapping = {
        sort lastActived: "desc"
    }

    static transients = ['previewComments']
    
    def getPreviewComments() {
        def previewComments = Comment.createCriteria().list(max: 3) {
            eq('post', this)
            eq('enabled', true)
            order('dateCreated', 'desc')
        }

        [comments: previewComments, totalComments: previewComments.totalCount]
    }
    
}