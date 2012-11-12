package opendream.infoaid.domain

class Expertise implements Comparable {

	String name
	String description

	static belongsTo = User
	static hasMany = [users: User]

	public int compareTo(other) {
		return name <=> other?.name
	}

	static mapping = {
		sort name: 'asc'
	}

	static constraints = {
		name unique: true
		description blank: true, nullable: true
	}
}