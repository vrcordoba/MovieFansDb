(function() {
    'use strict';

    angular
        .module('movieFansDbApp')
        .controller('MovieFetcherDialogController', MovieFetcherDialogController);

    MovieFetcherDialogController.$inject = ['$scope', '$uibModalInstance', 'entity', 'Movie'];

    function MovieFetcherDialogController($scope, $uibModalInstance, entity, Movie) {
        var vm = this;

        vm.title = entity.title;
        vm.clear = clear;
        vm.fetch = fetch;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function fetch () {
            vm.isFetching = true;
            Movie.fetch(vm.title, onFetchSuccess, onFetchError);
        }

        function onFetchSuccess (result) {
            $scope.$emit('movieFansDbApp:movieUpdate', result);
            $uibModalInstance.close(result);
            vm.isFetching = false;
        }

        function onFetchError () {
            vm.isFetching = false;
        }
    }
})();
