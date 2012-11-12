class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
        "/page/$slug/about"(controller: "page", action: "about")
        "/page/$slug/disable_page"(controller: "page", action: "disablePage")
        "/page/$slug/info"(controller: "page", action: "info")
        "/page/$slug/join_us"(controller: "page", action: "joinUs")
        "/page/$slug/leave_by"(controller: "page", action: "leavePage")
        "/page/$slug/limit_need/$limit"(controller: "page", action: "limitNeed")
        "/page/$slug/map"(controller: "page", action: "map")
        "/page/$slug/members"(controller: "page", action: "member")
        "/page/$slug/need"(controller: "page", action: "need")        
        "/page/$slug/post_message"(controller: "page"){ action =[POST:"postMessage"] }
        "/page/$slug/post_need"(controller: "page"){ action =[POST:"postNeed"] }
        "/page/$slug/post_resource"(controller: "page"){ action =[POST:"postResource"] }
        "/page/$slug/recent_post"(controller: "page", action: "recentPost")
        "/page/$slug/status"(controller: "page", action: "status") // default by recent posts
        "/page/$slug/top_members"(controller: "page", action: "topMember")
        "/page/$slug/top_post"(controller: "page", action: "topPost")
        "/page/$slug/update_page"(controller: "page", action: "updatePage")
        "/page/$slug/item_history"(controller: "page", action: "itemHistory")
        "/page/create_page"(controller: "page"){ action = [POST:"createPage"] }
        "/post/comment"(controller: "page"){action = [POST:"postComment"] }
        "/post/$postId/comments"(controller: "post"){action = [GET:"comment"] }
        "/post/$postId/delete"(controller: "page", action: "disablePost")
        "/comment/$commentId/delete"(controller: "page", action: "disableComment")
        "/home/$userId/recent-post"(controller: "home", action: "recentPost")
        "/home/$userId/top-post"(controller: "home", action: "topPost")
        "/front_page/info"(controller: "frontPage", action: "info")
        "/user/create"(controller: "user", action: "create")
        "/user/availableExpertises"(controller: "user", action: "availableExpertises")
        "/user/$userId/basic_info"(controller: "user"){ action = [GET:"showBasicInfo", PUT:"updateBasicInfo"] }
        "/user/$userId/joining"(controller: "user", action: "getPages")
        "/user/$userId/password"(controller: "user"){ action = [PUT:"updatePassword"] }
        "/user/$userId/expertises"(controller: "user") {
                action = [POST:"updateExpertises"]
        }
        "/"(view:"/index")
        "500"(view:'/error')
        }
}
