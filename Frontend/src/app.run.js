(function(){
    angular
        .module('app')
        .run(runApp);

    runApp.$inject=['$sessionStorage', 'loginService', '$location'];

    function runApp($sessionStorage, loginService, $location){
        if($sessionStorage.user){
            loginService.setUser($sessionStorage.user);
        }else{
            $location.path('/home');
        }
    }
})();