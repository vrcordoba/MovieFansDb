(function() {
    'use strict';

    angular
        .module('movieFansDbApp')
        .controller('ReviewController', ReviewController);

    ReviewController.$inject = ['$scope', '$state', 'entity', 'Review', 'Movie', 'User'];

    function ReviewController ($scope, $state, entity, Review, Movie, User) {
        var vm = this;

        vm.filter = entity;
        vm.filterReviews = filterReviews;
        vm.reviews = [];
        vm.movies = Movie.query();
        vm.users = User.query();

        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

        filterReviews();

        function filterReviews() {
            if(vm.filter.date) {
                var date = vm.filter.date;
                date.setMinutes(date.getMinutes() - date.getTimezoneOffset());
                var dateStr = date.toISOString().slice(0,10);
                if(vm.filter.author && vm.filter.movie) {
                    Review.query({
                        author:vm.filter.author.login,
                        date:dateStr,
                        movieId:vm.filter.movie.id},
                        function(result) {
                            vm.reviews = result;
                    });
                } else if(vm.filter.author) {
                    Review.query({
                        author:vm.filter.author.login,
                        date:dateStr},
                        function(result) {
                            vm.reviews = result;
                    });
                } else if(vm.filter.movie) {
                    Review.query({
                        date:dateStr,
                        movieId:vm.filter.movie.id},
                        function(result) {
                            vm.reviews = result;
                    });
                } else {
                    Review.query({date:dateStr},
                            function(result) {
                        vm.reviews = result;
                    });
                }
            } else {
                if(vm.filter.author && vm.filter.movie) {
                    Review.query({
                        author:vm.filter.author.login,
                        movieId:vm.filter.movie.id},
                        function(result) {
                            vm.reviews = result;
                    });
                } else if(vm.filter.author) {
                    Review.query({author:vm.filter.author.login},
                        function(result) {
                            vm.reviews = result;
                    });
                } else if(vm.filter.movie) {
                    Review.query({movieId:vm.filter.movie.id},
                        function(result) {
                            vm.reviews = result;
                    });
                } else {
                    Review.query(function(result) {
                        vm.reviews = result;
                    });
                }
            }
        }
    }
})();
