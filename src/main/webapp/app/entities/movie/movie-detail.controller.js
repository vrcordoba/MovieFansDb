(function() {
    'use strict';

    angular
        .module('movieFansDbApp')
        .controller('MovieDetailController', MovieDetailController);

    MovieDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Movie', 'Actor', 'Director', 'Review'];

    function MovieDetailController($scope, $rootScope, $stateParams, previousState, entity, Movie, Actor, Director, Review) {
        var vm = this;

        vm.movie = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('movieFansDbApp:movieUpdate', function(event, result) {
            vm.movie = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
