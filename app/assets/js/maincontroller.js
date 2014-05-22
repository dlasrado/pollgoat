app.controller("MainController", function($scope){
    	$scope.understand = "I now understand how the scope works!";
        $scope.inputValue = "";
});

function Hello($scope, $http) {
    $http.get('http://rest-service.guides.spring.io/greeting').
        success(function(data) {
            $scope.greeting = data;
        });
}