(function() {
    'use strict';

    angular
        .module('movieFansDbApp')
        .controller('ActorDetailController', ActorDetailController);

    ActorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Actor', 'Movie', 'User'];

    function ActorDetailController($scope, $rootScope, $stateParams, previousState, entity, Actor, Movie, User) {
        var vm = this;

        vm.actor = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('movieFansDbApp:actorUpdate', function(event, result) {
            vm.actor = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
