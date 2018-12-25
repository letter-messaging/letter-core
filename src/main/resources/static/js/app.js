'use strict';

const API_URL = "";
const MESSAGES_PER_PAGE = 20;
const LOCAL_STORAGE_TOKEN_NAME = "Auth-Token";

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

app.directive('scrolly', function () {
	return {
		restrict: 'A',
		link: function (scope, element, attrs) {
			let raw = element[0];

			element.bind('scroll', function () {
				if (raw.scrollTop === 0) {
					scope.$apply(attrs.scrolly);
				}
			});
		}
	};
});

app.directive('showAtt', function () {
	return {
		restrict: 'A',
		link: (scope, element, attrs) => {
			element.on('mouseover', () => {
				scope.$apply(() => {
					scope.showAttachmentsMenu = true;
					scope.cancelAttachmentsMenu();
				})
			});
			element.on('mouseout', () => {
				scope.closeAttachmentsMenu();
			});
		}
	};
});

app.controller('MainController', ($document, $scope, $http, $timeout) => {

	$document.bind('keydown', (e) => {
		if (e.keyCode === 27) {
			$scope.state = "out";
			$scope.currentConversationId = null;
			$scope.isLeftView = true;

			$scope.$apply();
		}
	});

	$scope.dateFormat = 'H:mm';

	$scope.ME = null;
	$scope.FULL_MESSAGE = null;

	$scope.token = null;
	$scope.credentials = {
		"login": null,
		"password": null,
		"confirmPassword": null,
		"firstName": null,
		"lastName": null
	};

	$scope.previews = [];
	$scope.messages = [];

	$scope.currentPreview = null;

	$scope.selectedMessages = [];

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

	/**
	 * out - outside certain conversation
	 * in - in certain conversation
	 * empty - in empty conversation
	 * @type {string}
	 */
	$scope.state = "out";

	$scope.messageText = null;

	$scope.listen = false;

	$scope.isLeftView = true;

	$scope.showAttachmentsMenu = false;
	$scope.showAttachmentsMenuTimeout = null;

	// init
	(() => {
		// auto login
		if (localStorage.getItem(LOCAL_STORAGE_TOKEN_NAME)) {
			const unverifiedToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_NAME);

			$http({
				url: API_URL + "/auth/validate",
				method: "GET",
				headers: {
					"Auth-Token": unverifiedToken
				}
			}).then(
				(response) => {
					$scope.credentials.login = response.data.login;

					$scope.token = unverifiedToken;
					$scope.initialize();
					$scope.updatePreviews();
					$scope.isAuth = false;

					$scope.listen = true;
					$scope.getNewMessages();
					$scope.getConversationRead();

					$scope.isLoaded = true;
				},
				(error) => {
					$scope.isLoaded = true;
				});
		} else {
			$scope.isLoaded = true;
		}

		$http({
			url: API_URL + "/message/init/full",
			method: "GET"
		}).then((response) => {
			$scope.FULL_MESSAGE = response.data;
		});
	})();

	$scope.login = () => {
		$http({
			url: API_URL + "/auth",
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
			localStorage.setItem(LOCAL_STORAGE_TOKEN_NAME, $scope.token);
			$scope.initialize();
			$scope.updatePreviews();

			$scope.listen = true;
			$scope.getNewMessages();
			$scope.getConversationRead();

			$scope.isAuth = false;
		});
	};

	$scope.register = () => {
		if ($scope.credentials.password === $scope.credentials.confirmPassword) {
			$http({
				url: API_URL + "/register",
				method: "POST",
				data: {
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
			url: API_URL + "/auth/validate",
			method: "GET",
			headers: {
				"Auth-Token": $scope.token
			}
		}).then((response) => {
			$scope.ME = response.data;
		});

		$scope.updatePreviews();
	};

	$scope.updatePreviews = () => {
		$http({
			url: API_URL + "/preview/all",
			method: "GET",
			headers: {
				"Auth-Token": $scope.token
			}
		}).then((response) => {
			response.data = response.data.filter((p) => p.lastMessage != null);

			response.data.sort((a, b) => moment(a.lastMessage.message.sent).isAfter(b.lastMessage.message.sent) ? -1 : 1);

			for (let preview of response.data) {
				if (preview.lastMessage) {
					preview.lastMessage.mine = preview.lastMessage.sender.user.login === $scope.ME.user.login;
				}
			}
			$scope.previews = response.data;
		});
	};

	$scope.openConversation = (preview) => {
		$scope.currentPreview = preview;

		$scope.searchText = "";
		$scope.isSearch = false;
		$scope.isLeftView = false;

		$http({
			url: API_URL + "/message/get",
			method: "GET",
			headers: {
				"Auth-Token": $scope.token
			},
			params: {
				"conversationId": preview.conversation.id,
				"offset": 0,
				"amount": MESSAGES_PER_PAGE
			}
		}).then((response) => {
			$scope.currentConversationId = preview.conversation.id;

			for (let message of response.data) {
				message.mine = $scope.ME.user.login === message.sender.user.login;
				message.status = "received";
			}
			$scope.messages = response.data;

			if ($scope.messages.length === 0) {
				$scope.state = "empty";
			} else {
				$scope.state = "in";
			}

			scrollToBottom(document.getElementsByClassName("message-wrapper")[0]);

			$scope.currentConversationWith = preview.with;

			$scope.updatePreviews();
		});
	};

	$scope.conversationsOrSearch = () => {
		if ($scope.isSearch) {
			if ($scope.searchText[0] === '@') return $scope.searchUsers;
			return $scope.searchConversations;
		} else {
			return $scope.previews;
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
				url: API_URL + "/search/users",
				method: "GET",
				headers: {
					"Auth-Token": $scope.token
				},
				params: {
					"search": $scope.searchText
				}
			}).then((response) => {
				$scope.searchUsers = response.data;
			});
		} else {
			$scope.isUserSearch = false;
			$http({
				url: API_URL + "/search/conversations",
				method: "GET",
				headers: {
					"Auth-Token": $scope.token
				},
				params: {
					"search": $scope.searchText
				}
			}).then((response) => {
				$scope.searchConversations = response.data;
			});
		}
	};

	$scope.createConversation = (user) => {
		$http({
			url: API_URL + "/conversation/create",
			method: "GET",
			headers: {
				"Auth-Token": $scope.token
			},
			params: {
				"with": user.user.login
			}
		}).then((response) => {
			$scope.searchText = "";
			$scope.isUserSearch = false;
			$scope.isSearch = false;

			let conversation = response.data;

			$scope.updatePreviews();

			$http({
				url: API_URL + "/preview/get",
				method: "GET",
				headers: {
					"Auth-Token": $scope.token
				},
				params: {
					"conversationId": conversation.id
				}
			}).then((response) => {
				$scope.openConversation(response.data);
			});
		});
	};

	$scope.sendMessage = () => {
		if ($scope.messageText.replace(/\s/g, '').length === 0) return;

		let messageText = $scope.messageText;
		$scope.messageText = "";

		let message = $scope.FULL_MESSAGE;
		message.conversation.id = $scope.currentConversationId;
		message.message.conversationId = $scope.currentConversationId;
		message.message.text = messageText;
		message.sender = $scope.ME;
		message.mine = true;
		message.status = "sending";

		$scope.messages.unshift(message);

		let preview = $scope.previews.find(c => c.conversation.id === message.conversation.id);
		if (preview != null) preview.lastMessage = message;

		$http({
			url: API_URL + "/messaging/send",
			method: "POST",
			data: message.message,
			headers: {
				"Auth-Token": $scope.token
			}
		}).then((response) => {
			$scope.messages = $scope.messages.filter(m => m !== message);
			$scope.updatePreviews();
		});
	};

	$scope.enterMessage = (e) => {
		if (e.which === 13 && !e.ctrlKey) {
			$scope.sendMessage();
		}
	};

	$scope.selectMessage = (message) => {
		if (message.selected === undefined) {
			message.selected = false;
			$scope.selectMessage(message);
			return;
		}

		if (message.selected === false) {
			message.selected = true;
			$scope.selectedMessages.push(message);
		} else {
			message.selected = false;
			$scope.selectedMessages.splice($scope.selectedMessages.indexOf(message), 1);
		}
	};

	$scope.clearSelectedMessages = () => {
		for (let m of $scope.selectedMessages) {
			m.selected = false;
		}

		$scope.selectedMessages = [];
	};

	$scope.deleteSelectedMessages = () => {
		$http({
			url: API_URL + "/message/delete",
			method: "POST",
			headers: {
				"Auth-Token": $scope.token
			},
			data: $scope.selectedMessages
		}).then(
			(response) => {
				$scope.updatePreviews();
				$scope.openConversation($scope.currentPreview);
				$scope.selectedMessages = [];
			},
			(error) => {
				$scope.updatePreviews();
				$scope.openConversation($scope.currentPreview);
				$scope.selectedMessages = [];
			});
	};

	$scope.getNewMessages = () => {
		if ($scope.listen) {
			$http({
				url: API_URL + "/messaging/get/m",
				method: "GET",
				headers: {
					"Auth-Token": $scope.token
				}
			}).then(
				(response) => {
					$scope.getNewMessages();

					let action = response.data;

					if (action.type === "NEW_MESSAGE") {
						let messageReceived = action.message;
						messageReceived.mine = $scope.ME.user.login === messageReceived.sender.user.login;
						messageReceived.status = "received";

						if ($scope.state === 'in' &&
							$scope.currentConversationId === messageReceived.conversation.id) {
							$scope.messages.unshift(messageReceived);
						}
					}

					// TODO: investigate
					$scope.updatePreviews();
					$scope.updatePreviews();
				},
				(error) => {
					$scope.getNewMessages();
				});
		}
	};

	$scope.getConversationRead = () => {
		if ($scope.listen) {
			$http({
				url: API_URL + "/messaging/get/r",
				method: "GET",
				headers: {
					"Auth-Token": $scope.token
				}
			}).then(
				(response) => {
					$scope.getConversationRead();

					let action = response.data;
					console.log(action);

					if (action.type === "CONVERSATION_READ") {
						if ($scope.currentConversationId === action.conversation.id) {
							for (let message of $scope.messages) {
								message.message.read = true;
							}
						}
					}

					// TODO: investigate
					$scope.updatePreviews();
					$scope.updatePreviews();
				},
				(error) => {
					$scope.getConversationRead();
				});
		}
	};

	$scope.logout = () => {
		localStorage.removeItem(LOCAL_STORAGE_TOKEN_NAME);

		$scope.previews = [];
		$scope.messages = [];

		$scope.searchConversations = [];
		$scope.searchUsers = [];

		$scope.isAuth = true;
		$scope.listen = false;
	};

	$scope.changeMobileView = () => {
		if (!$scope.isLeftView) {
			$scope.state = "out";
			$scope.currentConversationId = null;
		}

		$scope.isLeftView = !$scope.isLeftView;
	};

	$scope.loadMore = () => {
		$http({
			url: API_URL + "/message/get",
			method: "GET",
			headers: {
				"Auth-Token": $scope.token
			},
			params: {
				"conversationId": $scope.currentConversationId,
				"offset": $scope.messages.length,
				"amount": MESSAGES_PER_PAGE
			}
		}).then(
			(response) => {
				let newMessages = response.data;

				for (let message of newMessages) {

					message.mine = $scope.ME.user.login === message.sender.user.login;
					message.status = "received";

					$scope.messages.push(message);
				}
			});
	};

	$scope.closeAttachmentsMenu = () => {
		$scope.showAttachmentsMenuTimeout = $timeout(() => {
			$scope.showAttachmentsMenu = false;
		}, 1000);
	};

	$scope.cancelAttachmentsMenu = () => {
		$timeout.cancel($scope.showAttachmentsMenuTimeout);
	}

});