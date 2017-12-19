(function(){
    'use strict';

    angular
        .module('app.remindPassword')
        .controller('RemindPasswordController', RemindPasswordController);

    RemindPasswordController.$inject=['$scope','$http','$location', 'loginService', 'messageService'];

    function RemindPasswordController($scope,$http,$location, loginService, messageService){
        var vm=this;
        vm.submit=submit;
        vm.user={
            email: "",
            name: ""
        }
        init();
        
        //////////////////////////////
        
        function init(){ }

        function submit(){
            
            $http({method: 'POST', url: 'backend/users/remind', data: vm.user})
                .then(function(response){
                    console.log('Password reminded');
                    messageService.success("Check Your e-mail for the password.");
                }).catch(function(err){
                    console.log(err);
                    if(err.status==409){
                        messageService.error("There is no account with such data!");
                    }
                })
        }

    }
})();