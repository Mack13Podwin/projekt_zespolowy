(function(){
    'use strict';

    angular
        .module('app')
        .controller('MainController', MainController);

    MainController.$inject=['$scope', 'loginService'];

    function MainController($scope, loginService){
        var vm=this;
        vm.loggedIn=loginService.loggedIn;
    }

})();