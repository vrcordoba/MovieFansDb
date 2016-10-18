(function() {
    'use strict';

    angular
        .module('movieFansDbApp')
        .controller('MovieDialogController', MovieDialogController);

    MovieDialogController.$inject = ['$timeout', '$scope', 'Principal', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Movie', 'Actor', 'Director', 'Review'];

    function MovieDialogController ($timeout, $scope, Principal, $stateParams, $uibModalInstance, $q, entity, Movie, Actor, Director, Review) {
        var vm = this;

        vm.account = null;
        vm.movie = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.actors = Actor.query();
        vm.directors = Director.query();
        vm.reviews = Review.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.movie.id !== null) {
                Movie.update(vm.movie, onSaveSuccess, onSaveError);
            } else {
                Movie.save(vm.movie, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('movieFansDbApp:movieUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

        getAccount();

        function getAccount() {
            debugger;
            Principal.identity().then(function(account) {
                vm.account = account;
                console.log(vm.account.login)
                debugger;
            });
        }
    }
})();
