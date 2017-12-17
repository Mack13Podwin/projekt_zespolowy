(function(){
    'use strict';

    angular
        .module('app.changePassword')
        .controller('ChangePasswordController', ChangePasswordController);

    ChangePasswordController.$inject=['$scope','$http','$location'];

    function ChangePasswordController($scope,$http,$location){
        var vm=this;

        init();
        
        //////////////////////////////
        
        function init(){
            vm.submit=submit
          
        }
        function submit(isValid){
            
            // $http.get('/backend/remind/'+vm.login).then(function(res){
            //     growl.success('wysłano maila z hasłem')
            //     $location.path('/home')
            // })
        }

    }
})();