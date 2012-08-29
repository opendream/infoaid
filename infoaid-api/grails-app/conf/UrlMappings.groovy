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
        "/$slug/need"(controller: "page", action: "need")
        "/$slug/limitNeed/$limit"(controller: "page", action: "limitNeed")
        "/$slug/about"(controller: "page", action: "about")
        "/$slug/join_us/$userId"(controller: "page", action: "joinUs")
        "/main/info"(controller: "page", action: "summaryInfo")
		"/"(view:"/index")
		"500"(view:'/error')
	}
}
