dba_app.config(['$routeProvider', function ($routeProvider) {
    $routeProvider
        .when('/search', { templateUrl: 'resources/pages/search.html', controller: SearchController})
        .when('/statistics', { templateUrl: 'resources/pages/statistics.html'})
        .otherwise({redirectTo: '/home'});
}]);

function SearchController($scope, $http) {
    var rules = {
    };

    var setting = {
        onSuccess: function () {
            var queryData = {
                "depGroup": $("#dep_group").val() || ""
            };

            $scope.search = function () {
                $http.post('api/search').success(function (data) {
                    $scope.blogs = data;
                });
            };
        }
    };

    $('#searchForm').form(rules, setting);

    $('.ui.dropdown').dropdown({
        onChange: function (value) {
            $("#dep_group").val(value);
        }
    });

}