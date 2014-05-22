
var dependencies = [
    'ngRoute',
    'ui.bootstrap',
    'myApp.filters',
    'myApp.services',
    'myApp.controllers',
    'myApp.directives',
    'myApp.common',
    'myApp.routeConfig'
];

var app = angular.module('myApp', dependencies);

angular.module('myApp.routeConfig', ['ngRoute'])
    .config (function($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: '/assets/partials/view.html'
            })
            .when('/users/create', {
                templateUrl: '/assets/partials/create.html'
            })
            .otherwise({redirectTo: '/'});
        });

var commonModule = angular.module('myApp.common', []);
var controllersModule = angular.module('myApp.controllers', []);
var servicesModule = angular.module('myApp.services', []);
var modelsModule = angular.module('myApp.models', []);
var directivesModule = angular.module('myApp.directives', []);
var filtersModule = angular.module('myApp.filters', []);