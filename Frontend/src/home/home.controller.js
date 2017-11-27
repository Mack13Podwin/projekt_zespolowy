(function(){
    'use strict';

    angular
        .module('app.home')
        .controller('HomeController', HomeController);

    HomeController.$inject=['$scope', '$http'];

    function HomeController($scope, $http){
        var vm=this;
        vm.user={};
        vm.submit=submit;
        init();

        ///////////////////////
        function init(){}

        function submit(){
            console.log(vm.user);
            $http.post('/backend/users/login', {"login": vm.user.login, "password": vm.user.password})
                .then(function(response){
                    console.log(response.data);
                }).catch(function(err){
                    console.log(err);
                })
        }
    }
})();