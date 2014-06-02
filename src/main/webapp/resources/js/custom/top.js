function TopController($scope, $http) {
    $http.get('api/top').success(function (data) {
        $scope.tops = data;
    });

    $scope.show_blogs = function (top) {
        top.show = !top.show;
    }
}
