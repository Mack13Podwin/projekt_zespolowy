(function(){
    'use strict';

    angular
        .module('app.suggestedRecipe')
        .controller('SuggestedRecipeController', SuggestedRecipeController);

    SuggestedRecipeController.$inject=['$scope','$http','$location','loginService'];

    function SuggestedRecipeController($scope,$http,$location,loginService){
        var vm=this;
        var recipes=[
        {"id":0,"img":"img/1505668979131.jpeg","href":"http://www.foodnetwork.com/recipes/ree-drummond/baked-ziti-2312399","desc":"Baked Ziti"},
        {"id":1,"img":"img/1391138894606.jpeg","href":"http://www.foodnetwork.com/recipes/melissa-darabian/4-step-chicken-marengo-recipe-1949194","desc":"4-Step Chicken Marengo"},
        {"id":2,"img":"img/1371587349827.jpeg","href":"http://www.foodnetwork.com/recipes/patrick-and-gina-neely/easy-lemon-pasta-with-chicken-recipe-1911176","desc":"Easy Lemon Pasta with Chicken"},
        {"id":3,"img":"img/1371591136989.jpeg","href":"http://www.foodnetwork.com/recipes/one-dish-chicken-rice-bake-recipe-2118536","desc":"One Dish Chicken and Rice Bake"},
        {"id":4,"img":"img/1382545027777.jpeg","href":"http://www.foodnetwork.com/recipes/ina-garten/challah-french-toast-recipe-1940960","desc":"Challah French Toast"},
        {"id":5,"img":"img/1436542957030.jpeg","href":"http://www.foodnetwork.com/recipes/food-network-kitchen/creamy-shrimp-scampi-dip-3364256","desc":"Creamy Shrimp Scampi Dip"},
        {"id":6,"img":"img/1382541963105.jpeg","href":"http://www.foodnetwork.com/recipes/anne-burrell/pignoli-cookies-recipe-1947735","desc":"Pignoli Cookies"},
        {"id":7,"img":"img/1371610242996.jpeg","href":"http://www.foodnetwork.com/recipes/jamie-deen/jamies-old-fashioned-ginger-crinkle-cookies-recipe-2109075","desc":"Jamie's Old-Fashioned Ginger Crinkle Cookies"}
        ]
        var columnOne=recipes.filter((x)=>x.id%2==0)
        var columnTwo=recipes.filter((x)=>x.id%2==1)
        vm.recipesColumns=[]
        columnOne.forEach(function(curr,index){
            vm.recipesColumns.push([curr,columnTwo[index]])
        })

        vm.recommendedRecipes=[
            {"id":0,"img":"img/1382541357316.jpeg","href":"http://www.foodnetwork.com/recipes/bobby-flay/salmon-with-brown-sugar-and-mustard-glaze-recipe-1926380","desc":"Salmon with Brown Sugar and Mustard Glaze"},
            {"id":1,"img":"img/1371591017679.jpeg","href":"http://www.foodnetwork.com/recipes/food-network-kitchen/garlic-roasted-chicken-recipe-2104193","desc":"Food Network Kitchen's Garlic-Roasted Chicken"},
            {"id":2,"img":"img/1431766598136.jpeg","href":"http://www.foodnetwork.com/recipes/tyler-florence/chicken-marsala-recipe-1951778","desc":"Chicken Marsala"},
            {"id":3,"img":"img/1382375822579.jpeg","href":"http://www.foodnetwork.com/recipes/food-network-kitchen/pancakes-recipe-1913844","desc":"Pancakes"}
        ]

        vm.recipesLeft=[
            {"id":0,"href":"http://www.foodnetwork.com/holidays-and-parties/packages/holidays/holiday-central-christmas","desc":"Christmas Recipes"},
            {"id":1,"href":"http://www.foodnetwork.com/holidays-and-parties/packages/holidays/holiday-central-appetizers","desc":"Holiday Appetizers"},
            {"id":2,"href":"http://www.foodnetwork.com/recipes/packages/12-days-of-cookies","desc":"Holiday Cookies"},
            {"id":3,"href":"http://www.foodnetwork.com/recipes/packages/recipes-for-kids/kids-weeknight-dinners","desc":"Family Weeknight Dinners"},
            {"id":4,"href":"http://www.foodnetwork.com/healthy/packages/healthy-every-week/healthy-mains","desc":"Healthy Weeknight Dinners"},
            {"id":5,"href":"http://www.foodnetwork.com/recipes/packages/weeknight-dinners-winter","desc":"Winter Weeknight Dinners"},
            {"id":6,"href":"http://www.foodnetwork.com/topics/dinner","desc":"Dinner Ideas"},
            {"id":7,"href":"http://www.foodnetwork.com/topics/chicken","desc":"Chicken Recipes"},
        ]
        init();
        
        //////////////////////////////
        function init(){
            
        }
    }
})();