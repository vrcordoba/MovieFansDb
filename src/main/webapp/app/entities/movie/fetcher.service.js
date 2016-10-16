(function() {
    'use strict';
    angular
        .module('movieFansDbApp')
        .factory('Fetcher', Fetcher);

    Fetcher.$inject = ['$resource', 'DateUtils'];

    function Fetcher ($resource, DateUtils) {
        var resourceUrl = 'api/movies/fetcher';

        return $resource(resourceUrl, {}, {
            'fetch': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
