package opendream.infoaid.domain

import org.apache.commons.lang.builder.HashCodeBuilder

class PageUser implements Serializable {

	Page page
	Users user
	Relation relation

    static constraints = {
    	page blank: false
    	user blank: false
    	relation inList: Relation.list()
    }

    public enum Relation {
    	OWNER,
    	MEMBER
    	static list() {
    		[OWNER,MEMBER]
    	}
    }

    int hashCode() {
		def builder = new HashCodeBuilder()
		if (user) builder.append(user.id)
		if (role) builder.append(role.id)
		builder.toHashCode()
	}


}
