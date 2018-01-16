(function(){
    'use strict';

    angular
        .module('app.changePassword')
        .controller('ChangePasswordController', ChangePasswordController);

    ChangePasswordController.$inject=['$scope','$http','$location', 'loginService', 'messageService'];

    function ChangePasswordController($scope,$http,$location, loginService, messageService){
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
                $http({method: 'PATCH', url: 'backend/users/password/change', data: vm.user})
                    .then(function(response){
                        messageService.success("Your password was successfully changed!");
                        $location.path('/home');
                    }).catch(function(err){
                        console.log(err);
                        if(err.status==409){
                            messageService.error("Old password wasn't the real old password!");
                        }
                    })
            }else{
                console.log("Passwords don't match");
                messageService.warning("Repeat password and new password are not the same!")
            }
        }

    }
})();