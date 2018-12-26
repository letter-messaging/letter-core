app.component('message', {
	templateUrl: 'app/message/message.html',
	controller: function MessageController($scope) {
	},
	bindings: {
		message: '=',
		dateFormat: '=',
		isForwarded: '='
	}
});