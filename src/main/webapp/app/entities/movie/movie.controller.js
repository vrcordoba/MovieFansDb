(function() {
    'use strict';

    angular
        .module('movieFansDbApp')
        .controller('MovieController', MovieController);

    MovieController.$inject = ['$scope', '$state', 'Movie'];

    function MovieController ($scope, $state, Movie) {
        var vm = this;
        
        vm.movies = [];

        loadAll();

        function loadAll() {
            Movie.query(function(result) {
                vm.movies = result;
            });
        }
    }
})();
