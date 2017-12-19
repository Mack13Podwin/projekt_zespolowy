(function(){
    'use strict';

    angular
        .module('app.changeLogin')
        .controller('ChangeLoginController', ChangeLoginController);

    ChangeLoginController.$inject=['$scope','$http','$location', 'loginService', 'messageService'];

    function ChangeLoginController($scope,$http,$location, loginService, messageService){
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
                    messageService.success("Login was successfully changed");
                    $location.path('home');
                }).catch(function(err){
                    console.log(err);
                    if(err.status==409){
                        messageService.error("Old loginw wasn't the real old login!");
                    }
                })
        }

    }
})();