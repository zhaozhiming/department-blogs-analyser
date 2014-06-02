var dba_app = angular.module('dba', ['ngRoute']);

dba_app.config(['$routeProvider', function ($routeProvider) {
    $routeProvider
        .when('/search', { templateUrl: 'resources/pages/search.html', controller: SearchController})
        .when('/statistics', { templateUrl: 'resources/pages/statistics.html', controller: StatisticsController})
        .when('/top', { templateUrl: 'resources/pages/top.html', controller: TopController})
        .otherwise({redirectTo: '/home'});
}]);

$(document).ready(function () {
    $('#sideBarMenu').click(function () {
        $('.ui.sidebar').sidebar('toggle');
    });

    $('.ui.sidebar').sidebar('toggle');
});


