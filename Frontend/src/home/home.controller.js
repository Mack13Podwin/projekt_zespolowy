(function(){
    'use strict';

    angular
        .module('app.home')
        .controller('HomeController', HomeController);

    HomeController.$inject=['$scope', '$http', '$location', 'loginService'];

    function HomeController($scope, $http, $location, loginService){
        var vm=this;
        vm.slides=[
            {"id":0,"text":"Why will intelligent fridge change your life?", "text2":"It reminds about products that will expire soon","src":"img/Image_2_1440x640.jpg"},
            {"id":1,"text":"Why will intelligent fridge change your life?", "text2":"It shows you products you should buy based on your shopping history","src":"img/Basket-with-tasty-dairy-products-on-wooden-tray-on-dark-background.jpg"},
            {"id":2,"text":"Why will intelligent fridge change your life?", "text2":"Lacking inspirations for dinner? No problem. Our device will suggest you something based on what you have in fridge (coming soon)","src":"img/landscape-1474822198-how-to-make-pancakes.jpg"}]
        vm.myInterval = 5000;
        vm.noWrapSlides = false;
        vm.active = 0;
        init();

        ///////////////////////
        function init(){}

        
    }
})();