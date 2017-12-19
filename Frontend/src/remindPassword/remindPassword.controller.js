(function(){
    'use strict';

    angular
        .module('app.remindPassword')
        .controller('RemindPasswordController', RemindPasswordController);

    RemindPasswordController.$inject=['$scope','$http','$location', 'loginService'];

    function RemindPasswordController($scope,$http,$location, loginService){
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
                }).catch(function(err){
                    console.log(err);
                })
        }

    }
})();