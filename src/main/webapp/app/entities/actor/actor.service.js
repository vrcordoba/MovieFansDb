(function() {
    'use strict';
    angular
        .module('movieFansDbApp')
        .factory('Actor', Actor);

    Actor.$inject = ['$resource'];

    function Actor ($resource) {
        var resourceUrl =  'api/actors/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
