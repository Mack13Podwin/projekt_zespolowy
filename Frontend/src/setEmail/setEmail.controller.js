(function(){
    'use strict';

    angular
        .module('app.setEmail')
        .controller('SetEmailController', SetEmailController);

    SetEmailController.$inject=['$scope', '$http', '$location', 'loginService'];

    function SetEmailController($scope, $http, $location, loginService){
        var vm=this;
        vm.user={
            email: ""
        }
        vm.submit=submit;
        //////////////////////////
        function submit(){
            $http({method: 'POST', url: 'backend/users/email', headers: {'authorization': loginService.getUserToken()}, data: vm.user.email})
                .then(function(response){
                    loginService.firstLogin();
                    $location.path('/home');
                }).catch(function(err){
                    console.log(err);
                })
        }
    }
})();