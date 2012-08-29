class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
        "/$slug/info"(controller: "page", action: "info")
        "/$slug/members"(controller: "page", action: "member")  
        "/$slug/status"(controller: "page", action: "status") // default by recent posts
        "/$slug/top-post"(controller: "page", action: "topPost")
        "/$slug/map"(controller: "page", action: "map")
        "/$slug/need"(controller: "page", action: "need")
        "/$slug/limitNeed/$limit"(controller: "page", action: "limitNeed")
        "/$slug/about"(controller: "page", action: "about")
        "/$slug/join_us/$userId"(controller: "page", action: "joinUs")
        "/home/recent-post"(controller: "home", action: "recentPost")
        "/home/top-post"(controller: "home", action: "topPost")
		"/"(view:"/index")
		"500"(view:'/error')
	}
}
