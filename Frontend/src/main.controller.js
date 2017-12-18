(function(){
    'use strict';

    angular
        .module('app')
        .controller('MainController', MainController);

    MainController.$inject=['$scope', '$location', 'loginService'];

    function MainController($scope, $location, loginService){
        var vm=this;
        vm.loggedIn=loginService.loggedIn;
        vm.logout=logout;
        //////////////////////////////////
        function logout(){
            loginService.setUser(null);
            $location.path('/home');
        }
    }

})();