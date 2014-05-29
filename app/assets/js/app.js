
var dependencies = [
    'ngRoute',
    'ui.bootstrap',
    'pollGoat.filters',
    'pollGoat.services',
    'pollGoat.controllers',
    'pollGoat.directives',
    'pollGoat.common',
    'pollGoat.routeConfig'
];

var app = angular.module('pollGoat', dependencies);

angular.module('pollGoat.routeConfig', ['ngRoute'])
    .config (function($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: '/assets/partials/view.html'
            })
            .when('/create', {
                templateUrl: '/assets/partials/create.html'
            })
            .when('/edit/:polletId', {
                templateUrl: '/assets/partials/edit.html'
            })
            .when('/profile', {
                templateUrl: '/assets/partials/profile.html'
            })
            .otherwise({redirectTo: '/'});
        });

var commonModule = angular.module('pollGoat.common', []);
var controllersModule = angular.module('pollGoat.controllers', []);
var servicesModule = angular.module('pollGoat.services', []);
var modelsModule = angular.module('pollGoat.models', []);
var directivesModule = angular.module('pollGoat.directives', []);
var filtersModule = angular.module('pollGoat.filters', []);