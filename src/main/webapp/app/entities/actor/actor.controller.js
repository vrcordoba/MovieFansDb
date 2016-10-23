(function() {
    'use strict';

    angular
        .module('movieFansDbApp')
        .controller('ActorController', ActorController);

    ActorController.$inject = ['$scope', '$state', 'entity', 'Actor', 'User'];

    function ActorController ($scope, $state, entity, Actor, User) {
        var vm = this;

        vm.filter = entity;
        vm.filterActors = filterActors;
        vm.actors = [];
        vm.users = User.query();

        filterActors();

        function filterActors() {
            if(vm.filter.name && vm.filter.creator) {
                Actor.query({name:vm.filter.name, creator:vm.filter.creator.login}, function(result) {
                    vm.actors = result;
                });
            } else if(vm.filter.creator) {
                Actor.query({creator:vm.filter.creator.login}, function(result) {
                    vm.actors = result;
                });
            } else if(vm.filter.name) {
                Actor.query({name:vm.filter.name}, function(result) {
                    vm.actors = result;
                });
            } else {
                Actor.query(function(result) {
                    vm.actors = result;
                });
            }
        }
    }
})();
