(function() {
    'use strict';
    angular
        .module('movieFansDbApp')
        .factory('Director', Director);

    Director.$inject = ['$resource'];

    function Director ($resource) {
        var resourceUrl =  'api/directors/:id';

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
