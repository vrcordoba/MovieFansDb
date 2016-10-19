(function() {
    'use strict';

    angular
        .module('movieFansDbApp')
        .controller('ActorDialogController', ActorDialogController);

    ActorDialogController.$inject = ['$timeout', '$scope', 'Principal', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Actor', 'Movie'];

    function ActorDialogController ($timeout, $scope, Principal, $stateParams, $uibModalInstance, $q, entity, Actor, Movie) {
        var vm = this;

        vm.actor = entity;
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
            if (vm.actor.id !== null) {
                Actor.update(vm.actor, onSaveSuccess, onSaveError);
            } else {
                Actor.save(vm.actor, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('movieFansDbApp:actorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        getCreator();

        function getCreator() {
            Principal.identity().then(function(account) {
                vm.actor.creator = account.login;
            });
        }
    }
})();
