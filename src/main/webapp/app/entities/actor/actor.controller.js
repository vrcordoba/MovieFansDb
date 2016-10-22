(function() {
    'use strict';

    angular
        .module('movieFansDbApp')
        .controller('ActorController', ActorController);

    ActorController.$inject = ['$scope', '$state', 'entity', 'Actor'];

    function ActorController ($scope, $state, entity, Actor) {
        var vm = this;

        vm.filter = entity;
        vm.filterActors = filterActors;
        vm.actors = [];

        filterActors();

        function filterActors() {
            if(vm.filter.name && vm.filter.creator) {
                debugger;
                Actor.query({name:vm.filter.name, creator:vm.filter.creator}, function(result) {
                    vm.actors = result;
                });
            } else if(vm.filter.creator) {
                debugger;
                Actor.query({creator:vm.filter.creator}, function(result) {
                    vm.actors = result;
                });
            } else if(vm.filter.name) {
                debugger;
                Actor.query({name:vm.filter.name}, function(result) {
                    vm.actors = result;
                });
            } else {
                debugger;
                Actor.query(function(result) {
                    vm.actors = result;
                });
            }
        }
    }
})();
