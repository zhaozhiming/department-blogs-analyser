dba_app.config(['$routeProvider', function ($routeProvider) {
    $routeProvider
        .when('/search', { templateUrl: 'resources/pages/search.html', controller: SearchController})
        .when('/statistics', { templateUrl: 'resources/pages/statistics.html', controller: StatisticsController})
        .when('/top', { templateUrl: 'resources/pages/top.html', controller: TopController})
        .otherwise({redirectTo: '/home'});
}]);

var transform = function(data){
    return $.param(data);
};

var config = {
    headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
    transformRequest: transform
};

function StatisticsController($scope, $http) {
    var options = {
        selectedYear: (new Date).getFullYear()
    };

    $('#statistics_date').monthpicker(options);

    $scope.statistics = function () {
        var queryData = {
            "statisticsDate": $("#statistics_date").val() || ""
        };

        $http.post('api/statistics', queryData, config).success(function (data) {
            $scope.months = data;
        });
    };
}
