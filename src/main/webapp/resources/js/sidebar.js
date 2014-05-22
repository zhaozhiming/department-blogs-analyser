dba_app.config(['$routeProvider', function ($routeProvider) {
    $routeProvider
        .when('/search', { templateUrl: 'resources/pages/search.html', controller: SearchController})
        .when('/statistics', { templateUrl: 'resources/pages/statistics.html'})
        .otherwise({redirectTo: '/home'});
}]);

function SearchController($scope, $http) {
    var transform = function(data){
        return $.param(data);
    };

    var postConfig = {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
        transformRequest: transform
    };

    var rules = {
    };

    var setting = {
        onSuccess: function () {
            var queryData = {
                "depGroup": $("#dep_group").val() || ""
            };

            $scope.search = function () {
                $http.post('api/search', queryData, postConfig).success(function (data) {
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