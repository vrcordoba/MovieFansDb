(function() {
    'use strict';

    angular
        .module('movieFansDbApp')
        .controller('DirectorController', DirectorController);

    DirectorController.$inject = ['$scope', '$state', 'entity', 'Director', 'User'];

    function DirectorController ($scope, $state, entity, Director, User) {
        var vm = this;
        
        vm.filter = entity;
        vm.filterDirectors = filterDirectors;
        vm.directors = [];
        vm.users = User.query();

        filterDirectors();

        function filterDirectors() {
            if(vm.filter.name && vm.filter.creator) {
                Director.query({
                    name:vm.filter.name,
                    creator:vm.filter.creator.login}, function(result) {
                    vm.directors = result;
                });
            } else if(vm.filter.creator) {
                Director.query({creator:vm.filter.creator.login},
                    function(result) {
                        vm.directors = result;
                    });
            } else if(vm.filter.name) {
                Director.query({name:vm.filter.name}, function(result) {
                    vm.directors = result;
                });
            } else {
                Director.query(function(result) {
                    vm.directors = result;
                });
            }
        }
    }
})();
