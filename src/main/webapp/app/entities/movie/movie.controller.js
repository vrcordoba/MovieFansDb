(function() {
    'use strict';

    angular
        .module('movieFansDbApp')
        .controller('MovieController', MovieController);

    MovieController.$inject = ['$scope', '$state', 'entity', 'Movie', 'Director'];

    function MovieController ($scope, $state, entity, Movie, Director) {
        var vm = this;

        vm.filter = entity;
        vm.filterMovies = filterMovies;
        vm.movies = [];
        vm.directors = Director.query();

        filterMovies();

        function filterMovies() {
            if(vm.filter.title && vm.filter.director) {
                Movie.query({title:vm.filter.title, directorId:vm.filter.director.id}, function(result) {
                    vm.movies = result;
                });
            } else if(vm.filter.director) {
                Movie.query({directorId:vm.filter.director.id}, function(result) {
                    vm.movies = result;
                });
            } else if(vm.filter.title) {
                Movie.query({title:vm.filter.title}, function(result) {
                    vm.movies = result;
                });
            } else {
                Movie.query(function(result) {
                    vm.movies = result;
                });
            }
        }
    }
})();
