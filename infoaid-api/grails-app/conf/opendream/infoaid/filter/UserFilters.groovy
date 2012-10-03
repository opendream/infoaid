package opendream.infoaid.filter

class UserFilters {
    def springSecurityService

    def filters = {
        all(controller:'*', action:'*') {
            before = {
                params.user = springSecurityService?.principal                
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
