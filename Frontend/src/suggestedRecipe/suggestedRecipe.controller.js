(function(){
    'use strict';

    angular
        .module('app.suggestedRecipe')
        .controller('SuggestedRecipeController', SuggestedRecipeController);

    SuggestedRecipeController.$inject=['$scope','$http','loginService','messageService'];

    function SuggestedRecipeController($scope,$http,loginService,messageService){
        var vm=this;
        vm.recipes=[];
        init();
        
        //////////////////////////////
        function init(){
            $http.get('backend/ui/recipe/'+loginService.getUserFridgeId())
                .then(response=>{
                    console.log(response.data.hits[0].recipe);
                    vm.recipes.push(response.data.hits[0].recipe);
                    console.log(vm.recipes[0].ingredientLines);
                }).catch(err=>{
                    console.log(err);
                    messageService.error('Unfortunately, the recipe cannot be loaded');
                });
            $http.get('backend/ui/recipe/'+loginService.getUserFridgeId())
                .then(response=>{
                    console.log(response.data.hits[0].recipe);
                    vm.recipes.push(response.data.hits[0].recipe);
                }).catch(err=>{
                    console.log(err);
                    messageService.error('Unfortunately, the recipe cannot be loaded');
                });
            $http.get('backend/ui/recipe/'+loginService.getUserFridgeId())
                .then(response=>{
                    console.log(response.data.hits[0].recipe);
                    vm.recipes.push(response.data.hits[0].recipe);
                }).catch(err=>{
                    console.log(err);
                    messageService.error('Unfortunately, the recipe cannot be loaded');
                });
        }
    }
})();