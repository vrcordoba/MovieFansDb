(function() {
    'use strict';

    angular
        .module('movieFansDbApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('movie', {
            parent: 'entity',
            url: '/movie',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'movieFansDbApp.movie.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/movie/movies.html',
                    controller: 'MovieController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('movie');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('movie-detail', {
            parent: 'entity',
            url: '/movie/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'movieFansDbApp.movie.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/movie/movie-detail.html',
                    controller: 'MovieDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('movie');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Movie', function($stateParams, Movie) {
                    return Movie.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'movie',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('movie-detail.edit', {
            parent: 'movie-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/movie/movie-dialog.html',
                    controller: 'MovieDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Movie', function(Movie) {
                            return Movie.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('movie.new', {
            parent: 'movie',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/movie/movie-dialog.html',
                    controller: 'MovieDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                imdbId: null,
                                title: null,
                                date: null,
                                plot: null,
                                rating: null,
                                genre: null,
                                id: null,
                                creator: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('movie', null, { reload: 'movie' });
                }, function() {
                    $state.go('movie');
                });
            }]
        })
        .state('movie.fetchfrominternet', {
            parent: 'movie',
            url: '/fetchfrominternet',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/movie/movie-fetcher-dialog.html',
                    controller: 'MovieFetcherDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('movie', null, { reload: 'movie' });
                }, function() {
                    $state.go('movie');
                });
            }]
        })
        .state('movie.edit', {
            parent: 'movie',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/movie/movie-dialog.html',
                    controller: 'MovieDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Movie', function(Movie) {
                            return Movie.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('movie', null, { reload: 'movie' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('movie.delete', {
            parent: 'movie',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/movie/movie-delete-dialog.html',
                    controller: 'MovieDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Movie', function(Movie) {
                            return Movie.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('movie', null, { reload: 'movie' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
