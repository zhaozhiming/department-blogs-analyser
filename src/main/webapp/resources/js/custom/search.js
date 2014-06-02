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
