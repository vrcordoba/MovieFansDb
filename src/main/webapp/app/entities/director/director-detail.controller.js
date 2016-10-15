(function() {
    'use strict';

    angular
        .module('movieFansDbApp')
        .controller('DirectorDetailController', DirectorDetailController);

    DirectorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Director', 'Movie', 'User'];

    function DirectorDetailController($scope, $rootScope, $stateParams, previousState, entity, Director, Movie, User) {
        var vm = this;

        vm.director = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('movieFansDbApp:directorUpdate', function(event, result) {
            vm.director = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
