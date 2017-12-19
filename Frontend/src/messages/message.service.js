(function(){
    angular
        .module('app.message')
        .service('messageService', messageService);

    messageService.$inject=['growl'];

    function messageService(growl){
        var service={
            warning: warning,
            success: success,
            error: error,
            info: info
        };
        return service;
        ////////////////////////
        function warning(msg){
            growl.warning(msg, {disableCountDown: true, ttl: 3000});
        }

        function error(msg){
            growl.error(msg, {disableCountDown: true, ttl: 3000});
        }

        function success(msg){
            growl.success(msg, {disableCountDown: true, ttl: 3000});
        }

        function info(msg){
            growl.info(msg, {disableCountDown: true, ttl: 3000});
        }
    }
})();