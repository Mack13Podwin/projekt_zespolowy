(function(){

    angular
        .module('app.setEmail')
        .controller('SetEmailController', SetEmailController);

    SetEmailController.$inject=['$scope', '$http', 'loginService'];

    function SetEmailController($scope, $http, loginService){
        var vm=this;
        vm.user={
            email: ""
        }
        vm.submit=submit;
        //////////////////////////
        function submit(){
            $http.post('backend/users/email', vm.user.email, {'authorization': loginService.getUserToken()})
                .then(function(response){

                }).cach(function(err){
                    console.log(err);
                })
        }
    }
})();