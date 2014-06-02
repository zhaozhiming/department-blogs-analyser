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

function SearchController($scope, $http, $route) {
    var rules = {
    };

    var setting = {
        onSuccess: function () {
            var queryData = {
                "depGroup": $("#dep_group").val() || "",
                "website": $("#website").val() || "",
                "startDate": $("#start_date").val() || "",
                "endDate": $("#end_date").val() || ""
            };

            $scope.search = function () {
                $http.post('api/search', queryData, config).success(function (data) {
                    $scope.blogs = data;
                });
            };
        }
    };

    $('#searchForm').form(rules, setting);

    $('#reset_button').click(function() {
        $route.reload();
    });

    $('#group_selection').dropdown({
        onChange: function (value) {
            $("#dep_group").val(value);
        }
    });

    $('#website_selection').dropdown({
        onChange: function (value) {
            $("#website").val(value);
        }
    });

    $('#start_date').pickadate({
        format: 'yyyy-mm-dd'
    });

    $('#end_date').pickadate({
        format: 'yyyy-mm-dd'
    });
}

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

function TopController($scope, $http) {
    $http.get('api/top').success(function (data) {
        $scope.tops = data;
    });

    $scope.show_blogs = function (top) {
        top.show = !top.show;
    }
}