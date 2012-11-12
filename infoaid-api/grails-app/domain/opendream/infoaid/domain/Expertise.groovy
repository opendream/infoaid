package opendream.infoaid.domain

class Expertise {

	String name
	String description

	static belongsTo = User
	static hasMany = [users: User]

	static constraints = {
		name unique: true
		description blank: true, nullable: true
	}
}