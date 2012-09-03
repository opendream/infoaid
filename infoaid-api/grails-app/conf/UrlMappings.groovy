class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
        "/page/$slug/about"(controller: "page", action: "about")
        "/page/$slug/disablePage"(controller: "page", action: "disablePage")
        "/page/$slug/info"(controller: "page", action: "info")
        "/page/$slug/join_us/$userId"(controller: "page", action: "joinUs")
        "/page/$slug/leavePage"(controller: "page", action: "leavePage")
        "/page/$slug/limitNeed/$limit"(controller: "page", action: "limitNeed")
        "/page/$slug/map"(controller: "page", action: "map")
        "/page/$slug/members"(controller: "page", action: "member")  
        "/page/$slug/need"(controller: "page", action: "need")
        "/page/$slug/postComment"(controller: "page", action: "postComment")
        "/page/$slug/status"(controller: "page", action: "status") // default by recent posts
        "/page/$slug/topMembers"(controller: "page", action: "topMember")        
        "/page/$slug/topPost"(controller: "page", action: "topPost")
        "/page/$slug/updatePage"(controller: "page", action: "updatePage")
        "/page/createPage"(controller: "page", action: "createPage")
        "/home/recent-post"(controller: "home", action: "recentPost")
        "/home/top-post"(controller: "home", action: "topPost")
        "/main/info"(controller: "page", action: "summaryInfo")
        "/user/create"(controller: "user", action: "create")
        "/user/basicInfo"(controller: "user"){ action = [GET:"showBasicInfo", PUT:"updateBasicInfo"] }
        "/user/joining"(controller: "user", action: "getPages")
        "/user/password"(controller: "user"){ action = [PUT:"updatePassword"] }
        "/"(view:"/index")
        "500"(view:'/error')
        }
}
