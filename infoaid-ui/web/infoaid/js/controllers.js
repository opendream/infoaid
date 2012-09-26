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