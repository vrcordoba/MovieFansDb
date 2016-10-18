(function() {
    'use strict';

    angular
        .module('movieFansDbApp')
        .controller('ReviewDetailController', ReviewDetailController);

    ReviewDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Review', 'Movie'];

    function ReviewDetailController($scope, $rootScope, $stateParams, previousState, entity, Review, Movie) {
        var vm = this;

        vm.review = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('movieFansDbApp:reviewUpdate', function(event, result) {
            vm.review = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
