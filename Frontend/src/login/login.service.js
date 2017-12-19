(function(){
    angular
        .module('app.login')
        .service('loginService', loginService);

    loginService.$inject=['$http', '$sessionStorage'];

    function loginService($http, $sessionStorage){
        var user;
        var service={
            login: login,
            getUserFridgeId: getUserFridgeId,
            getUserToken: getUserToken,
            setUser: setUser,
            firstLogin: firstLogin,
            loggedIn: loggedIn
        }
        return service;
        ///////////////////////////////
        function login(login, password){
            return $http.post('/backend/users/login', {'login': login, 'password': password})
                        .then(function(response){
                            console.log(response.data);
                            setUser(response.data);
                            return response.data;
                        })
        }

        function getUserFridgeId(){
            return user.fridgeid;
        }

        function getUserToken(){
            return user.token;
        }

        function setUser(u){
            user=u;
            $sessionStorage.user=user;
        }

        function firstLogin(){
            user.firstLogin=false;
        }

        function loggedIn(){
            return !!user && !user.firstLogin;
        }
        
    }

})();