(function(){
    'use strict';

    angular
        .module('app.changeLogin')
        .controller('ChangeLoginController', ChangeLoginController);

    ChangeLoginController.$inject=['$scope','$http','$location', 'loginService'];

    function ChangeLoginController($scope,$http,$location, loginService){
        var vm=this;
        vm.submit=submit;
        vm.user={
            oldlogin: "",
            newlogin: ""
        }
        init();
        
        //////////////////////////////
        
        function init(){ }
        function submit(){
            $http({method: 'PATCH', url: 'backend/users/login/change', headers: {'authorization': loginService.getUserToken()}, data: vm.user})
                .then(function(response){
                    $location.path('home');
                }).catch(function(err){
                    console.log(err);
                })
        }

    }
})();