package opendream.infoaid.domain

class Role {

	String authority
	Status status = Status.ACTIVE

	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true
	}

	public enum Status {
        ACTIVE,
        INACTIVE
        static list() {
            [ACTIVE,INACTIVE]
        }
    }
}
