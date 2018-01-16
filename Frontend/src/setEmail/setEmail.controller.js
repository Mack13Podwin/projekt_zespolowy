(function(){
    'use strict';

    angular
        .module('app.setEmail')
        .controller('SetEmailController', SetEmailController);

    SetEmailController.$inject=['$scope', '$http', '$location', 'loginService', 'messageService'];

    function SetEmailController($scope, $http, $location, loginService, messageService){
        var vm=this;
        vm.user={
            email: ""
        }
        vm.submit=submit;
        //////////////////////////
        function submit(){
            $http({method: 'POST', url: 'backend/users/email', data: vm.user.email})
                .then(function(response){
                    messageService.success("E-mail successfully changed");
                    loginService.firstLogin();
                    $location.path('/home');
                }).catch(function(err){
                    console.log(err);
                })
        }
    }
})();