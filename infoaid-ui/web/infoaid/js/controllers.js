function ListCtrl($scope, Post) {
    var slug = $scope.slug;

    var lastRowDateCreated = function () {
        return $($scope.posts).last().get(0).dateCreated;
    };

    $scope.posts = Post.query({slug: slug});

    $scope.loadMore = function () {
        Post.query({
            slug: slug,
            until: lastRowDateCreated(),
            limit: 10
        }, function (posts) {
            angular.forEach(posts, function (post) {
                $scope.posts.push(post);
            });
        });
    };
}

function MemberCtrl($scope, $http) {  
    console.log('hello: '+$scope.slug);
    $http.get(baseUrl + '/api/members/'+$scope.slug).success(function(data) {
        $scope.members = data.topMembers;
    });
}