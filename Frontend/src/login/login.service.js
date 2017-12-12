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
            setUser: setUser,
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

        function setUser(u){
            user=u;
            $sessionStorage.user=user;
        }

        function loggedIn(){
            return !!user;
        }
        
    }

})();