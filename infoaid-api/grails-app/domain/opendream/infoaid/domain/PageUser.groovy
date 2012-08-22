package opendream.infoaid.domain

import org.apache.commons.lang.builder.HashCodeBuilder

class PageUser implements Serializable {

	Page page
	Users user

    static constraints = {
    	page blank: false
    	user blank: false
    }

    int hashCode() {
		def builder = new HashCodeBuilder()
		if (user) builder.append(user.id)
		if (role) builder.append(role.id)
		builder.toHashCode()
	}

	
}
