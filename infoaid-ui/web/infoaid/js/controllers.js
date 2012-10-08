angular.module('headerService', ['ngResource']).
    factory('JoinPage', function ($resource) {
        var JoinPage = $resource(baseUrl + '/page/joinPage');        
        return JoinPage;
    }).
    factory('LeavePage', function ($resource) {
        var LeavePage = $resource(baseUrl + '/page/leavePage');
        return LeavePage;
    }).
    filter('JoinLabel', function() {
        return function(input) {
            return input ? 'Leave Page' : 'Join Page';
        };
    }).
    factory('SharedService', function($rootScope) {
        var SharedService = {};
        
        SharedService.isJoined = '';

        SharedService.prepForBroadcast = function(isJoined, slug, page, userId, user) {
            this.isJoined = isJoined;
            this.slug = slug;
            this.page = page;
            this.userId = userId;
            this.user = user;
            this.broadcastItem();
        };

        SharedService.broadcastItem = function() {
            $rootScope.$broadcast('isJoinedBroadcast');
        };

        return SharedService;
    }).
    factory('PostsBroadcast', function($rootScope) {
        var PostsBroadcast = {};
        
        //PostsBroadcast.isJoined = '';

        PostsBroadcast.prepForBroadcast = function(target, posts) {
            this.target = target;
            this.posts = posts;
            //this.option = option;            
            this.broadcastItem();
        };

        PostsBroadcast.broadcastItem = function() {
            $rootScope.$broadcast('postsBroadcast');
        };

        return PostsBroadcast;
    });


function PostListCtrl($scope, Post, RearrangePost, PostsBroadcast) {
    var slug = $scope.slug;    

    var lastRowDateCreated = function () {
        var date = new Date($($scope.posts).last().get(0).lastActived);
        var yyyy = date.getFullYear();
        var m = date.getMonth()+1;
        var dd = date.getDate();
        var hh = date.getHours();
        var mm = date.getMinutes();
        return yyyy+'-'+m+'-'+dd+' '+hh+':'+mm;        
    };
    $scope.posts = Post($scope.target).query({slug: slug});

    $scope.$on('postsBroadcast', function() {
        if ($scope.target) {
            $scope.posts = [];
        }
        RearrangePost($scope, PostsBroadcast.posts);      
    });
}

function MemberCtrl($scope, $http, SharedService, Post, PostsBroadcast) {  
    var limit = InfoAid.settings.pageSidebarTopMemberLimit;
    $http.get(baseUrl + '/api/members/'+$scope.slug).success(function(data) {
        $scope.members = data.topMembers.slice(0, limit);
    });

    $scope.$on('isJoinedBroadcast', function() {
        if(SharedService.isJoined==0) {
            var i = findMember(SharedService.userId);
            $scope.members.splice(i, 1);
        } else {
           $scope.members.unshift(SharedService.user).slice(0, limit);
        }
    });

    
    function findMember(userId) {
        for(var i=0; i<$scope.members.length; i++) {
            if($scope.members[i].id==userId) {
                return i;
            }
        }
        return 0;
    }
    
    $scope.loadItem = function(id) {
        if($scope.target == 'item_history'){
            $scope.target = 'recent_post';
        } else {
            $scope.target = 'item_history';
        }

        Post($scope.target).query({
            itemId: id,
            slug: $scope.slug,
            until: '',
            limit: 10
        }, function (posts) {
            PostsBroadcast.prepForBroadcast($scope.target, posts);      
        });
    }
}

function SidebarCtrl($scope, $http, SharedService) {  
    $http.get(baseUrl + '/api/getPages').success(function(data) {
        $scope.pages = data.pages;
    });

    $scope.$on('isJoinedBroadcast', function() {
        if(SharedService.isJoined==0) {
            var i = findPage(SharedService.slug);
            $scope.pages.splice(i, 1);
        } else {
           $scope.pages.unshift(SharedService.page);
        }

    });

    function findPage(slug) {
        for(var i=0; i<$scope.pages.length; i++) {
            if($scope.pages[i].slug==slug) {
                return i;
            }
        }
        return 0;
    }
}

function HeaderCtrl($scope, JoinPage, LeavePage, SharedService) {    

    $scope.handleClick = function() {
        var options = {
                slug: $scope.slug
            };

        if($scope.isjoined==1) { //leave page
            LeavePage.get(options, function (ret) {   
                if(ret.status==1) {
                    $scope.isjoined = 0; 
                    SharedService.prepForBroadcast(0, ret.slug, null, ret.userId);
                }
            });
        } else { // join page
            JoinPage.get(options, function (ret) { 
                if(ret.status==1) {
                    $scope.isjoined = 1;                     
                    SharedService.prepForBroadcast(1, ret.slug, ret.page, ret.user.id, ret.user);
                }
            });
        }        
    }
}