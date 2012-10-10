angular.module('page', ['pageSearchService', 'headerService']);

    function searchController($scope, Page) {
        var opts = {
          lines: 13, // The number of lines to draw
          length: 7, // The length of each line
          width: 4, // The line thickness
          radius: 10, // The radius of the inner circle
          corners: 1, // Corner roundness (0..1)
          rotate: 0, // The rotation offset
          color: '#000', // #rgb or #rrggbb
          speed: 1, // Rounds per second
          trail: 60, // Afterglow percentage
          shadow: false, // Whether to render a shadow
          hwaccel: false, // Whether to use hardware acceleration
          className: 'spinner', // The CSS class to assign to the spinner
          zIndex: 2e9, // The z-index (defaults to 2000000000)
          top: 'auto', // Top position relative to parent in px
          left: 'auto' // Left position relative to parent in px
        };
        $scope.word = '';
        $scope.pages = [];
        $scope.search = function() {
            if($scope.word != '' && $scope.word != null && $scope.word.length >= 2) {
                angular.element('#load-more').hide();
                var target = document.getElementById('loading');
                var spinner = new Spinner(opts).spin(target);
                var pages = Page.query({
                    word: $scope.word,
                    offset: 0
                }, function(resp) {
                    if(resp.length == 0) {
                        //$('#load-more').hide();
                        angular.element('#result-search-error').addClass("text-error");
                        angular.element('#result-search-error').html('Not found this place');
                    } else {
                        angular.element('#result-search-error').html('');
                        //$('#load-more').show();
                    }
                    spinner.stop()
                });
                $scope.pages = pages;
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
            var target = document.getElementById('loading');
            var spinner = new Spinner(opts).spin(target);
            var pages = Page.query({
                word: '',
                offset: 0
            }, function(resp) {
                if(resp.length == 0) {
                    //$('#load-more').hide();
                    angular.element('#result-search-error').addClass("text-error");
                    angular.element('#result-search-error').html('Not found this place');
                } else {
                    angular.element('#result-search-error').html('');
                    //$('#load-more').show();
                }
                spinner.stop()
            });
            $scope.pages = pages;                
        }();
    }