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
        email nullable: true
        telNo nullable: true
    }

    List getPages() {
        PageUser.findAllByUser(this).collect { it.page } as List
    }
    
    /*
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
    */
}
