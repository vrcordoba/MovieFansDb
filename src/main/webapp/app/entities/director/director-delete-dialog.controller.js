(function() {
    'use strict';

    angular
        .module('movieFansDbApp')
        .controller('DirectorDeleteController',DirectorDeleteController);

    DirectorDeleteController.$inject = ['$uibModalInstance', 'entity', 'Director'];

    function DirectorDeleteController($uibModalInstance, entity, Director) {
        var vm = this;

        vm.director = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Director.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
