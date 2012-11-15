package opendream.infoaid.domain

import org.apache.commons.lang.builder.HashCodeBuilder

class PageUser implements Serializable {

	Page page
	User user
	Relation relation
    Integer conversation = 0

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

    static PageUser createPage(User user, Page page, Relation relation = Relation.OWNER, boolean flush = false) {
        new PageUser(user: user, page: page, relation: relation).save(flush: flush, insert: true)
    }

    static PageUser joinPage(User user, Page page, Relation relation = Relation.MEMBER, boolean flush = false) {
        new PageUser(user: user, page: page, relation: relation).save(flush: flush, insert: true)
    }

    static PageUser leavePage(User user, Page page) {
        def pageUser = PageUser.findByPageAndUser(page, user)
        pageUser.delete(flush: true)
    }

    static PageUser get(long userId, long pageId) {
        PageUser.find 'from PageUser where user.id=:userId and page.id=:pageId',
            [userId: userId, pageId: pageId]
    }

    static PageUser removeUserFromPage(User user, Page page) {
        def pageUser = PageUser.findByPageAndUser(page, user)
        pageUser.delete(flush: true)
    }

    static PageUser setRelation(User user, Page page, String relation) {
        def relationEnum
        if(relation.tr( 'A-Z', 'a-z') == 'owner') {
            relationEnum = Relation.OWNER
        } else {
            relationEnum = Relation.MEMBER
        }
        def pageUser = PageUser.findByPageAndUser(page, user)
        pageUser.relation = relationEnum
        pageUser.save(flush: true)
    }

    int hashCode() {
        def builder = new HashCodeBuilder()
        if (user) builder.append(user.id)
        if (page) builder.append(page.id)
        builder.toHashCode()
    }
}
