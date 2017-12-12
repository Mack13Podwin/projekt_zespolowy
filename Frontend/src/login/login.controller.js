(function(){
    'use strict';

    angular
        .module('app.login')
        .controller('LoginController', LoginController);

    LoginController.$inject=['$scope', '$http', '$location', 'loginService'];

    function LoginController($scope, $http, $location, loginService){
        var vm=this;
        vm.user={
            'login': "",
            'password': ""
        };
        vm.submit=submit;
        init();

        ///////////////////////
        function init(){}

        function submit(){
            loginService.login(vm.user.login, vm.user.password)
                .then(function(user){
                    console.log("Hurra");
                    $location.path('/home');

                }).catch(function(err){
                    console.log(err);
                })
        }
    }
})();