class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
        "/$slug/info"(controller: "page", action: "info")
        "/$slug/members"(controller: "page", action: "member")  
        "/$slug/topMembers"(controller: "page", action: "topMember")
        "/$slug/status"(controller: "page", action: "status")
        "/$slug/map"(controller: "page", action: "map")
        "/$slug/need"(controller: "page", action: "need")
        "/$slug/limitNeed/$limit"(controller: "page", action: "limitNeed")
        "/$slug/about"(controller: "page", action: "about")
        "/$slug/join_us/$userId"(controller: "page", action: "joinUs")
        "/main/info"(controller: "page", action: "summaryInfo")
        "/createPage"(controller: "page", action: "createPage")
        "/$slug/leavePage"(controller: "page", action: "leavePage")
        "/$slug/postComment"(controller: "page", action: "postComment")
		"/"(view:"/index")
		"500"(view:'/error')
	}
}
