package opendream.infoaid.domain

class Comment {

	String message
	Date dateCreated
    Date lastUpdated

    static belongsTo = [member:Member]

    static constraints = {
    	message blank: false
    }
}
