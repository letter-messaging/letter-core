const MESSAGES_PER_PAGE = 20;

let app = angular.module("app", []);

let token;
let login;

app.controller('MainController', ($http, $scope) => {

	$scope.conversations = [];
	$scope.messages = [];

	(() => {
		login = prompt("login:");
		let password = prompt("password:");

		token = $http({
			url: "/auth",
			method: "GET",
			params: {
				"login": login,
				"password": password
			},
			transformResponse: undefined
		}).then((response) => {
			token = response.data;
			$scope.init(token);
		});

	})();

	$scope.init = (token) => {
		$http({
			url: "/preview/all",
			method: "GET",
			params: {
				"token": token
			}
		}).then((response) => {
			for (let conversation of response.data) {
				conversation.lastMessage.sent = moment(new Date(conversation.lastMessage.sent)).format("hh:MM");
			}
			$scope.conversations = response.data;
		});
	};

	$scope.openConversation = (conversationId) => {
		$http({
			url: "/message/get",
			method: "GET",
			params: {
				"token": token,
				"conversationId": conversationId,
				"offset": 0,
				"amount": MESSAGES_PER_PAGE
			}
		}).then((response) => {
			for (let message of response.data) {
				message.message.sent = moment(new Date(message.message.sent)).format("hh:MM");

				message.mine = login === message.sender.login;
			}
			$scope.messages = response.data;
		});
	}

});