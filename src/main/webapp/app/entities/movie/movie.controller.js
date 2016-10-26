(function() {
    'use strict';

    angular
        .module('movieFansDbApp')
        .controller('MovieController', MovieController);

    MovieController.$inject = ['$scope', '$state', 'entity', 'Movie', 'Actor', 'Director'];

    function MovieController ($scope, $state, entity, Movie, Actor, Director) {
        var vm = this;

        vm.filter = entity;
        vm.filterMovies = filterMovies;
        vm.movies = [];
        vm.actors = Actor.query();
        vm.directors = Director.query();

        filterMovies();

        function filterMovies() {
            if(vm.filter.title && vm.filter.actor && vm.filter.director) {
                Movie.query({title:vm.filter.title,
                    actorId:vm.filter.actor.id,
                    directorId:vm.filter.director.id}, function(result) {
                    vm.movies = result;
                });
            } else if (vm.filter.title && vm.filter.actor) {
                Movie.query({title:vm.filter.title,
                    actorId:vm.filter.actor.id}, function(result) {
                    vm.movies = result;
                });
            } else if(vm.filter.title && vm.filter.director) {
                Movie.query({title:vm.filter.title,
                    directorId:vm.filter.director.id}, function(result) {
                    vm.movies = result;
                });
            } else if (vm.filter.actor && vm.filter.director) {
                Movie.query({actorId:vm.filter.actor.id,
                    directorId:vm.filter.director.id}, function(result) {
                    vm.movies = result;
                });
            } else if(vm.filter.title) {
                Movie.query({title:vm.filter.title}, function(result) {
                    vm.movies = result;
                });
            } else if(vm.filter.actor) {
                Movie.query({actorId:vm.filter.actor.id}, function(result) {
                    vm.movies = result;
                });
            } else if(vm.filter.director) {
                Movie.query({directorId:vm.filter.director.id}, function(result) {
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
