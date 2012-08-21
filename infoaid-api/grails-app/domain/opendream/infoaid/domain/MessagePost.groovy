package opendream.infoaid.domain

class MessagePost extends Post {

    String message

    static constraints = {
        message blank: false
    }
}
