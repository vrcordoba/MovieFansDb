(function() {
    'use strict';

    angular
        .module('movieFansDbApp')
        .controller('ReviewDialogController', ReviewDialogController);

    ReviewDialogController.$inject = ['$timeout', '$scope', 'Principal', '$stateParams', '$uibModalInstance', 'entity', 'Review', 'Movie'];

    function ReviewDialogController ($timeout, $scope, Principal, $stateParams, $uibModalInstance, entity, Review, Movie) {
        var vm = this;

        vm.review = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
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
            if (vm.review.id !== null) {
                Review.update(vm.review, onSaveSuccess, onSaveError);
            } else {
                Review.save(vm.review, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('movieFansDbApp:reviewUpdate', result);
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

        getAuthor();

        function getAuthor() {
            Principal.identity().then(function(account) {
                vm.review.author = account.login;
            });
        }
    }
})();
