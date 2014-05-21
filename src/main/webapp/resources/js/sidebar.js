dba_app.config(['$routeProvider', function ($routeProvider) {
    $routeProvider
        .when('/search', { templateUrl: 'resources/pages/search.html', controller: SearchController})
        .when('/statistics', { templateUrl: 'resources/pages/statistics.html'})
        .otherwise({redirectTo: '/home'});
}]);

function SearchController($scope, $http) {
    $scope.search = function () {
        $http.post('api/search').success(function (data) {
            $scope.blogs = data;
        });
    }
}