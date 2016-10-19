(function() {
    'use strict';

    angular
        .module('movieFansDbApp')
        .controller('DirectorDialogController', DirectorDialogController);

    DirectorDialogController.$inject = ['$timeout', '$scope', 'Principal', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Director', 'Movie'];

    function DirectorDialogController ($timeout, $scope, Principal, $stateParams, $uibModalInstance, $q, entity, Director, Movie) {
        var vm = this;

        vm.director = entity;
        vm.clear = clear;
        vm.save = save;
        vm.movies = Movie.query();

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

        getCreator();

        function getCreator() {
            Principal.identity().then(function(account) {
                vm.director.creator = account.login;
            });
        }
    }
})();
