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
    });


function ListCtrl($scope, Post, RearrangePost) {
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

    $scope.posts = Post.query({slug: slug});

    $scope.loadMore = function () {
        Post.query({
            slug: slug,
            until: lastRowDateCreated(),
            limit: 10
        }, function (posts) {
            RearrangePost($scope, posts);            
        });
    };
}

function MemberCtrl($scope, $http) {  
    $http.get(baseUrl + '/api/members/'+$scope.slug).success(function(data) {
        $scope.members = data.topMembers;
    });
}

function HeaderCtrl($scope, JoinPage, LeavePage) {    

    $scope.handleClick = function() {
        var options = {
                slug: $scope.slug
            };

        if($scope.isjoined==1) {
            LeavePage.get(options, function (ret) {   
                if(ret.status==1) {
                    $scope.isjoined = 0; 
                }
            });
        } else {
            JoinPage.get(options, function (ret) { 
                if(ret.status==1) {
                    $scope.isjoined = 1; 
                }
            });
        }        
    }
}