(function(){
    'use strict';

    angular
        .module('app', ['ngRoute',
            'ngStorage',
            'app.message',
            'app.login',
            'app.remindPassword',
            'app.changePassword',
            'app.changeLogin',
            'app.setEmail',
            'app.fridgeItems',
            'app.shoppingList',
            'app.suggestedRecipe',
            'app.home',
            'ngAnimate',
            'ui.bootstrap'
        ]);
})();