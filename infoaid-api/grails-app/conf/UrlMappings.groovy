class UrlMappings {

        static mappings = {
                "/$controller/$action?/$id?"{
                        constraints {
                                // apply constraints here
                        }
                }
        "/$slug/about"(controller: "page", action: "about")
        "/$slug/info"(controller: "page", action: "info")
        "/$slug/join_us/$userId"(controller: "page", action: "joinUs")
        "/$slug/leavePage"(controller: "page", action: "leavePage")
        "/$slug/limitNeed/$limit"(controller: "page", action: "limitNeed")
        "/$slug/map"(controller: "page", action: "map")
        "/$slug/members"(controller: "page", action: "member")  
        "/$slug/need"(controller: "page", action: "need")
        "/$slug/postComment"(controller: "page", action: "postComment")
        "/$slug/status"(controller: "page", action: "status") // default by recent posts
        "/$slug/topMembers"(controller: "page", action: "topMember")
        "/$slug/leavePage"(controller: "page", action: "leavePage")
        "/$slug/updatePage"(controller: "page", action: "updatePage")
        "/$slug/disablePage"(controller: "page", action: "disablePage")
        "/$slug/topPost"(controller: "page", action: "topPost")
        "/createPage"(controller: "page", action: "createPage")
        "/home/recent-post"(controller: "home", action: "recentPost")
        "/home/top-post"(controller: "home", action: "topPost")
        "/main/info"(controller: "page", action: "summaryInfo")
                "/"(view:"/index")
                "500"(view:'/error')
        }
}
