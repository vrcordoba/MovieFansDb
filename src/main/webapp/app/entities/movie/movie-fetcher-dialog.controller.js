(function() {
    'use strict';

    angular
        .module('movieFansDbApp')
        .controller('MovieFetcherDialogController', MovieFetcherDialogController);

    MovieFetcherDialogController.$inject = ['$uibModalInstance', 'entity', 'Fetcher'];

    function MovieFetcherDialogController($uibModalInstance, entity, Fetcher) {
        var vm = this;

        vm.title = entity.title;
        vm.clear = clear;
        vm.fetch = fetch;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function fetch () {
            vm.isFetching = true;
            Fetcher.fetch(vm.title, onFetchSuccess, onFetchError);
        }

        function onFetchSuccess (result) {
            $scope.$emit('movieFansDbApp:movieFetch', result);
            $uibModalInstance.close(result);
            vm.isFetching = false;
        }

        function onFetchError () {
            vm.isFetching = false;
        }
    }
})();
