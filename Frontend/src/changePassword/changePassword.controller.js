(function(){
    'use strict';

    angular
        .module('app.changePassword')
        .controller('ChangePasswordController', ChangePasswordController);

    ChangePasswordController.$inject=['$scope','$http','$location', 'loginService'];

    function ChangePasswordController($scope,$http,$location, loginService){
        var vm=this;
        vm.submit=submit;
        vm.user={
            oldpassword: "",
            newpassword: ""
        };
        vm.repeatnewpassword= ""
        init();
        
        //////////////////////////////
        
        function init(){ }
        function submit(){
            if(vm.user.newpassword==vm.repeatnewpassword){
                $http({method: 'PATCH', url: 'backend/users/password/change', headers: {'authorization': loginService.getUserToken()}, data: vm.user})
                    .then(function(response){
                        $location.path('/home');
                    }).catch(function(err){
                        console.log(err);
                    })
            }else{
                console.log("Passwords don't match");
            }
        }

    }
})();