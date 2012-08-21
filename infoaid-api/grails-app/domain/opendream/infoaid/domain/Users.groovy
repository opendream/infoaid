package opendream.infoaid.domain

class Users {

	String username
	String password
	String firstname
	String lastname
	String email
	String telNo
	Date dateCreated
	Date lastUpdated

    static constraints = {
    	username blank: false, unique: true
		password blank: false
    }
}
