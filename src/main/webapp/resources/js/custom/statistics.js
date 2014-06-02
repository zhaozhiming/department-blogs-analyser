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
