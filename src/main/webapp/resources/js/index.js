var dba_app = angular.module('dba', ['ngRoute']);

$(document).ready(function () {
    $('#sideBarMenu').click(function(){
        $('.ui.sidebar').sidebar('toggle');
    })

    $('.ui.sidebar').sidebar('toggle');
});


