angular.module('message', ['ngResource']).
	factory('Message', function ($resource) {
		var Message = $resource('http://localhost/api/page/:id');

		return Message;
	}).
	config(function ($routeProvider) {
		$routeProvider.
			when('/', {controller: ListCtrl, templateUrl:'post-messages'});
	});

function ListCtrl($scope, Message) {
	$scope.messages = Message.query();
}