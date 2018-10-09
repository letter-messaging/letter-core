const MESSAGES_PER_PAGE = 20;

let app = angular.module("app", []);

app.controller('MainController', ($document, $scope, $http) => {

	$scope.token = null;
	$scope.credentials = {
		"login": null,
		"password": null,
		"confirmPassword": null
	};

	$scope.conversations = [];
	$scope.messages = [];

	$scope.searchConversations = [];
	$scope.searchUsers = [];

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

	$document.bind('keydown', (e) => {
		if (e.keyCode === 27) {
			$scope.state = "out";
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
				transformResponse: undefined
			}).then(
				(response) => {
					$scope.token = token;
					$scope.initialize();
					$scope.isAuth = false;
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
			$scope.isAuth = false;
		});
	};

	$scope.register = () => {
		if ($scope.credentials.password === $scope.credentials.confirmPassword) {
			$http({
				url: "/auth",
				method: "GET",
				params: {
					"login": $scope.credentials.login,
					"password": $scope.credentials.password
				},
				transformResponse: undefined
			}).then((response) => {
				$scope.isAuth = false;
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
					preview.lastMessage.message.sent = moment(new Date(preview.lastMessage.message.sent)).format("hh:MM");
			}
			$scope.conversations = response.data;
		});
	};

	$scope.openConversation = (conversationId) => {
		$http({
			url: "/message/get",
			method: "GET",
			params: {
				"token": $scope.token,
				"conversationId": conversationId,
				"offset": 0,
				"amount": MESSAGES_PER_PAGE
			}
		}).then((response) => {
			for (let message of response.data) {
				message.message.sent = moment(new Date(message.message.sent)).format("hh:MM");

				message.mine = $scope.credentials.login === message.sender.user.login;
			}
			$scope.messages = response.data;

			if ($scope.messages.length === 0) {
				$scope.state = "empty";
			} else {
				$scope.state = "in";
			}
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
						preview.lastMessage.message.sent = moment(new Date(preview.lastMessage.message.sent)).format("hh:MM");
				}
				$scope.searchConversations = response.data;
			});
		}
	};

	$scope.createConversation = (withLogin) => {
		$http({
			url: "/conversation/create",
			method: "GET",
			params: {
				"token": $scope.token,
				"with": withLogin
			}
		}).then((response) => {
			let conversationId = response.data;

			$scope.initialize();
			$scope.openConversation(conversationId);
		});
	};

	$scope.logout = () => {
		localStorage.removeItem("token");

		$scope.conversations = [];
		$scope.messages = [];

		$scope.searchConversations = [];
		$scope.searchUsers = [];

		$scope.isAuth = true;
	}

});