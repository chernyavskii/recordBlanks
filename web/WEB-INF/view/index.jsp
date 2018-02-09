<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Бланки</title>
</head>
<body>
<div ng-app="app" ng-controller="indexController">
    <p>Логин: <input type="text" name="username" ng-model="username" required /></p>
    <p>Пароль: <input type="text" name="password" ng-model="password" required /></p>
    <button ng-click="login()">Войти</button>
</div>
</body>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular-resource.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular-route.min.js"></script>
<script src="/view/js/controllers/indexController.js"></script>
</html>
