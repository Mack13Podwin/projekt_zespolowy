(function(){
    'use strict';

    angular
        .module('app.remindPassword')
        .controller('RemindPasswordController', RemindPasswordController);

    RemindPasswordController.$inject=['$scope','$http','$location'];

    function RemindPasswordController($scope,$http,$location){
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