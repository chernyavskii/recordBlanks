var app = angular.module("app", ["ngRoute"]);
app.config(function($routeProvider)
{
    $routeProvider.when('/main',
    {
        templateUrl: '/main',
        controller: 'mainController'
    })
});
app.controller("indexController", function ($scope, $http, $location)
{
    $scope.login = function ()
    {
        var data =
        {
            username: $scope.username,
            password: $scope.password
        };
        $http.post('/login', data)
        .then(function (data)
        {
            $location.path("/main");
            console.log(data);
        })
        .catch(function (data)
        {
            console.log(data);
        });
    };
});