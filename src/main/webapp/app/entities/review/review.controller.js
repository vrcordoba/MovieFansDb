(function() {
    'use strict';

    angular
        .module('movieFansDbApp')
        .controller('ReviewController', ReviewController);

    ReviewController.$inject = ['$scope', '$state', 'Review'];

    function ReviewController ($scope, $state, Review) {
        var vm = this;
        
        vm.reviews = [];

        loadAll();

        function loadAll() {
            Review.query(function(result) {
                vm.reviews = result;
            });
        }
    }
})();
