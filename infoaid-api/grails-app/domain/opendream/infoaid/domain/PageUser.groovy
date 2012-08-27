package opendream.infoaid.domain

import org.apache.commons.lang.builder.HashCodeBuilder

class PageUser implements Serializable {

	Page page
	Users user
	Relation relation
    //Integer participation = 0

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

    static PageUser createPage(Users user, Page page, Relation relation = Relation.OWNER, boolean flush = false) {
        new PageUser(user: user, page: page, relation: relation).save(flush: flush, insert: true)
    }

    static PageUser joinPage(Users user, Page page, Relation relation = Relation.MEMBER, boolean flush = false) {
        new PageUser(user: user, page: page, relation: relation).save(flush: flush, insert: true)
    }

    static PageUser leavePage(Users user, Page page) {
        def pageUser = PageUser.findByPageAndUser(page, user)
        pageUser.delete()
    }

    int hashCode() {
        def builder = new HashCodeBuilder()
        if (user) builder.append(user.id)
        if (page) builder.append(page.id)
        builder.toHashCode()
    }
}
