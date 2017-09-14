/**
 * Created by prasanna_d on 9/13/2017.
 */
app.controller('mainController',['$scope',function ($scope) {
    var socket = io.connect('http://localhost:3000');

    $scope.onInit = function () {
        $scope.result = [];
        socket.on('chat message', function(msg){
            console.log(msg);
            $scope.result = msg.split("\n");
            console.log(msg.split("\n"));
            $scope.$apply();
        });
    };

}]);