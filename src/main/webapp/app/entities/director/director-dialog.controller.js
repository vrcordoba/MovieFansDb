(function() {
    'use strict';

    angular
        .module('movieFansDbApp')
        .controller('DirectorDialogController', DirectorDialogController);

    DirectorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Director', 'Movie', 'User'];

    function DirectorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Director, Movie, User) {
        var vm = this;

        vm.director = entity;
        vm.clear = clear;
        vm.save = save;
        vm.movies = Movie.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.director.id !== null) {
                Director.update(vm.director, onSaveSuccess, onSaveError);
            } else {
                Director.save(vm.director, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('movieFansDbApp:directorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
