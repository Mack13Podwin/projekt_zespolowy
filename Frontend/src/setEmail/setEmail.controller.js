(function(){

    angular
        .module('app.setEmail')
        .controller('SetEmailController', SetEmailController);

    SetEmailController.$inject=['$scope', '$http'];

    function SetEmailController($scope, $http){
        var vm=this;
        vm.submit=submit;
        //////////////////////////
        function submit(){
            
        }
    }
})();