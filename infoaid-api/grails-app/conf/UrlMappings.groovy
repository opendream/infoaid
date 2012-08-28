class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
        "/$slug/info"(controller: "page", action: "info")
        "/$slug/members"(controller: "page", action: "member")  
        "/$slug/status"(controller: "page", action: "status")
        "/$slug/map"(controller: "page", action: "map")
		"/"(view:"/index")
		"500"(view:'/error')
	}
}
