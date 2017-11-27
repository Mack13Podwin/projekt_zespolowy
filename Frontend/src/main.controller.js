(function(){
    'use strict';

    angular
        .module('app')
        .controller('MainController', MainController);

    MainController.$inject=['$scope', 'userService'];

    function MainController($scope, userService){
        var vm=this;
        vm.loggedIn=userService.loggedIn;
    }

})();