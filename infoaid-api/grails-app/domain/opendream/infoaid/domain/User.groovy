package opendream.infoaid.domain

class User {

	transient springSecurityService

	String username
	String password
	String firstname
    String lastname
    String email
    String telNo
	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	Date dateCreated
    Date lastUpdated

	static constraints = {
		username blank: false, unique: true
		password blank: false
		email nullable: true
        telNo nullable: true
	}

	static mapping = {
		table 'users'
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}

	List getPages() {
        PageUser.findAllByUser(this).collect { it.page } as List
    }

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}
}
