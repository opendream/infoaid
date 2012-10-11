angular.module('page', ['pageSearchService', 'headerService']);

    function searchController($scope, Page) {
        $scope.word = '';
        $scope.pages = [];

        $scope.search = function() {

            if ($scope.word != '' && $scope.word != null && $scope.word.length >= 2) {

                $scope.pages = [];
                $scope.loading = true;

                Page.query({
                    word: $scope.word,
                    offset: 0
                }, function(resp) {
                    $scope.loading = false;
                    $scope.pages = resp;
                });
            } else {
                angular.element('#search-body').addClass("error");
                angular.element('#word').tooltip({'title':'Please type 2 more character'}).tooltip('show');
            }
        }

        $scope.change = function() {
            if($scope.word != '' && $scope.word != null) {
                if($scope.word.length < 2) {
                    angular.element('#search-body').addClass("error");
                    angular.element('#word').tooltip({'title':'Please type 2 more character'}).tooltip('show');
                } else {
                    angular.element('#search-body').removeClass("error");
                    angular.element('#word').tooltip({'title':'Please type 2 more character'}).tooltip('hide');
                }
            }
        }

        $scope.loadMore = function(event) {
            var button = $(event.currentTarget);
            button.button('loading');

            Page.query({
                word: $scope.word,
                offset: $scope.pages.length
            }, function (pages) {
                if(pages.length == 0) {
                    $('#load-more-button').hide();  
                }
                angular.forEach(pages, function (page) {
                    $scope.pages.push(page);
                });

                button.button('reset');
            });
        };

        $scope.init = function() {
            $scope.loading = true;

            Page.query({
                word: '',
                offset: 0
            }, function(resp) {
                $scope.loading = false;
                $scope.pages = resp;
            });              
        }();
    }