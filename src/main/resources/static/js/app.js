const MESSAGES_PER_PAGE = 20;

let app = angular.module("app", []);

app.directive('myEnter', function () {
	return function (scope, element, attrs) {
		element.bind("keydown", function (event) {
			if (event.which === 13) {
				scope.$apply(function () {
					scope.$eval(attrs.myEnter);
				});

				event.preventDefault();
			}
		});
	};
});

app.controller('MainController', ($document, $scope, $http) => {

	$scope.token = null;
	$scope.credentials = {
		"login": null,
		"password": null,
		"confirmPassword": null,
		"firstName": null,
		"lastName": null
	};

	$scope.conversations = [];
	$scope.messages = [];

	$scope.searchConversations = [];
	$scope.searchUsers = [];

	$scope.currentConversationId = null;
	$scope.currentConversationWith = null;

	$scope.isLoaded = false;

	$scope.isAuth = true;
	$scope.isLogin = true;

	$scope.isSearch = false;
	$scope.isUserSearch = false;

	$scope.searchText = null;

	// out - outside certain conversation
	// in - in certain conversation
	// empty - in empty conversation
	$scope.state = "out";

	$scope.messageText = null;

	$scope.listen = false;

	$document.bind('keydown', (e) => {
		if (e.keyCode === 27) {
			$scope.state = "out";
			$scope.currentConversationId = null;

			$scope.$apply();
		}
	});

	// init
	(() => {
		// autologin
		if (localStorage.getItem("token")) {
			let token = localStorage.getItem("token");

			$http({
				url: "/auth/validate",
				method: "GET",
				params: {
					"token": token
				},
			}).then(
				(response) => {
					$scope.credentials.login = response.data.login;

					$scope.token = token;
					$scope.initialize();
					$scope.isAuth = false;

					$scope.listen = true;
					$scope.getMessages();

					$scope.isLoaded = true;
				},
				(error) => {
					$scope.isLoaded = true;
				});
		} else {
			$scope.isLoaded = true;
		}
	})();

	$scope.login = () => {
		$http({
			url: "/auth",
			method: "GET",
			params: {
				"login": $scope.credentials.login,
				"password": $scope.credentials.password
			},
			transformResponse: undefined
		}).then((response) => {
			// storing password is bad habit!
			$scope.credentials.password = null;

			$scope.token = response.data;
			localStorage.setItem("token", $scope.token);
			$scope.initialize();

			$scope.listen = true;
			$scope.getMessages();

			$scope.isAuth = false;
		});
	};

	$scope.register = () => {
		if ($scope.credentials.password === $scope.credentials.confirmPassword) {
			$http({
				url: "/register",
				method: "GET",
				params: {
					"firstName": $scope.credentials.firstName,
					"lastName": $scope.credentials.lastName,
					"login": $scope.credentials.login,
					"password": $scope.credentials.password
				},
			}).then((response) => {
				$scope.isLogin = true;
			});
		}
	};

	$scope.initialize = () => {
		$http({
			url: "/preview/all",
			method: "GET",
			params: {
				"token": $scope.token
			}
		}).then((response) => {
			for (let preview of response.data) {
				if (preview.lastMessage)
					preview.lastMessage.message.sent = moment(preview.lastMessage.message.sent).format("H:mm");
			}
			$scope.conversations = response.data;
		});
	};

	$scope.openConversation = (preview) => {
		$http({
			url: "/message/get",
			method: "GET",
			params: {
				"token": $scope.token,
				"conversationId": preview.conversation.id,
				"offset": 0,
				"amount": MESSAGES_PER_PAGE
			}
		}).then((response) => {
			$scope.currentConversationId = preview.conversation.id;

			for (let message of response.data) {
				message.message.sent = moment(message.message.sent).format("H:mm");

				message.mine = $scope.credentials.login === message.sender.user.login;
			}
			$scope.messages = response.data;

			if ($scope.messages.length === 0) {
				$scope.state = "empty";
			} else {
				$scope.state = "in";
			}

			$scope.currentConversationWith = preview.with;
		});
	};

	$scope.conversationsOrSearch = () => {
		if ($scope.isSearch) {
			if ($scope.searchText[0] === '@') return $scope.searchUsers;
			return $scope.searchConversations;
		} else {
			return $scope.conversations;
		}
	};

	$scope.searchForConversations = () => {
		$scope.isSearch = $scope.searchText.length !== 0;

		if (!$scope.isSearch) {
			$scope.isUserSearch = false;
			return;
		}

		if ($scope.searchText[0] === '@') {
			$scope.isUserSearch = true;
			$http({
				url: "/search/users",
				method: "GET",
				params: {
					"token": $scope.token,
					"search": $scope.searchText
				}
			}).then((response) => {
				$scope.searchUsers = response.data;
			});
		} else {
			$scope.isUserSearch = false;
			$http({
				url: "/search/conversations",
				method: "GET",
				params: {
					"token": $scope.token,
					"search": $scope.searchText
				}
			}).then((response) => {
				for (let preview of response.data) {
					if (preview.lastMessage)
						preview.lastMessage.message.sent = moment(preview.lastMessage.message.sent).format("H:mm");
				}
				$scope.searchConversations = response.data;
			});
		}
	};

	$scope.createConversation = (user) => {
		$http({
			url: "/conversation/create",
			method: "GET",
			params: {
				"token": $scope.token,
				"with": user.user.login
			}
		}).then((response) => {
			let conversation = response.data;

			$scope.initialize();

			$http({
				url: "/preview/get",
				method: "GET",
				params: {
					"token": $scope.token,
					"conversationId": conversation.id
				}
			}).then((response) => {
				$scope.openConversation(response.data);
			});
		});
	};

	$scope.sendMessage = () => {
		if ($scope.messageText.replace(/\s/g, '').length === 0) return;

		$http({
			url: "/message/init",
			method: "GET"
		}).then((response) => {
			let message = response.data;
			message.conversationId = $scope.currentConversationId;
			message.text = $scope.messageText;

			$http({
				url: "/messaging/send",
				method: "POST",
				data: message,
				params: {
					"token": $scope.token
				}
			}).then((response) => {
				$scope.messageText = "";
			});
		});
	};

	$scope.enterMessage = (e) => {
		if (e.which === 13 && !e.ctrlKey) {
			$scope.sendMessage();
		}
	};

	$scope.getMessages = () => {
		if ($scope.listen) {
			$http({
				url: "/messaging/get",
				method: "GET",
				params: {
					"token": $scope.token
				}
			}).then(
				(response) => {
					let messageReceived = response.data;

					messageReceived.message.sent = moment(messageReceived.message.sent).format("H:mm");
					console.log(messageReceived.message.sent);
					messageReceived.mine = $scope.credentials.login === messageReceived.sender.user.login;

					if ($scope.state === 'in' && $scope.currentConversationId === messageReceived.conversation.id) {
						$scope.messages.unshift(messageReceived);
					}

					$scope.initialize();
					$scope.getMessages();
				},
				(error) => {
					$scope.getMessages();
				});
		}
	};

	$scope.logout = () => {
		localStorage.removeItem("token");

		$scope.conversations = [];
		$scope.messages = [];

		$scope.searchConversations = [];
		$scope.searchUsers = [];

		$scope.isAuth = true;
		$scope.listen = false;
	}

});