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

app.controller('MainController', ($document, $scope, $http, $timeout, $window) => {

	$document.bind('keydown', (e) => {
		if (e.keyCode === 27) {
			$scope.state = "out";
			$scope.currentConversationId = null;
			$scope.isLeftView = true;

			$scope.$apply();
		}
	});

	/**
	 * Current date format used in all view dates
	 * @type {string}
	 */
	$scope.dateFormat = 'H:mm';

	/**
	 * UserDTO representing current client
	 * @type {null}
	 */
	$scope.ME = null;

	/**
	 * Template used for proper field insertion on message sending
	 * @type {null}
	 */
	$scope.FULL_MESSAGE = null;

	/**
	 * Current user token. Used in all authorized queries and stored in a local storage
	 * @type {null}
	 */
	$scope.token = null;

	/**
	 * Represent user credentials. AKA RegisterUserDTO
	 * @type {{firstName: null, lastName: null, password: null, confirmPassword: null, login: null}}
	 */
	$scope.credentials = {
		"login": null,
		"password": null,
		"confirmPassword": null,
		"firstName": null,
		"lastName": null
	};

	/**
	 * Previews array of current window
	 * @type {Array}
	 */
	$scope.previews = [];

	/**
	 * Messages array within current conversation. May change on conversation change
	 * @type {Array}
	 */
	$scope.messages = [];

	$scope.currentPreview = null;

	$scope.selectedMessages = [];

	/**
	 * Array of conversations displayed during the search
	 * @type {Array}
	 */
	$scope.searchConversations = [];

	/**
	 * Array of users displayed during the search
	 * @type {Array}
	 */
	$scope.searchUsers = [];

	$scope.currentConversationId = null;

	/**
	 * Became 'true' after init function completes
	 * @type {boolean}
	 */
	$scope.isLoaded = false;

	/**
	 * Display auth view if true
	 * @type {boolean}
	 */
	$scope.isAuth = true;

	/**
	 * Trigger between views.
	 * 'true' - login view
	 * 'false' - register view
	 * @type {boolean}
	 */
	$scope.isLogin = true;

	$scope.isSearch = false;
	$scope.isUserSearch = false;

	$scope.searchText = null;

	/**
	 * Demonstrate the state of current window.
	 * 'out' - outside certain conversation
	 * 'in' - in certain conversation
	 * 'empty' - in empty conversation
	 * @type {string}
	 */
	$scope.state = "out";

	/**
	 * Model used to enter message text content
	 * @type {null}
	 */
	$scope.messageText = null;

	/**
	 * Allow long-polling http requests when true, deny otherwise
	 * @type {boolean}
	 */
	$scope.listen = false;

	/**
	 * Trigger views on mobile responsive version
	 * 'true' - left view
	 * 'false' - right view
	 * @type {boolean}
	 */
	$scope.isLeftView = true;
	$scope.isEditMessageView = false;

	/**
	 * Store currently editing message if such is present
	 * @type {null}
	 */
	$scope.editingMessage = null;

	/**
	 * Used in displaying attachments menu
	 * @type {boolean}
	 */
	$scope.showAttachmentsMenu = false;

	/**
	 * Used in displaying attachments menu
	 * @type {null}
	 */
	$scope.showAttachmentsMenuTimeout = null;

	$scope.notification = new Audio("/sound/newmsg.mp3");

	/**
	 * Used in title to display count of new messages received when tab is in background
	 * @type {number}
	 */
	$scope.newMessageCount = 0;

	$scope.autoLogin = () => {
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
					$scope.isLoaded = true;

					$scope.listen = true;
					$scope.getNewMessages();
					$scope.getMessageEdits();
					$scope.getConversationRead();
				},
				(error) => {
					$scope.isLoaded = true;
				});
		} else {
			$scope.isLoaded = true;
		}
	};

	/**
	 * Called when script is loaded. Initialize whole variables and complete auto login
	 */
	(() => {
		$scope.autoLogin();

		$http({
			url: API_URL + "/message/init/full",
			method: "GET"
		}).then((response) => {
			$scope.FULL_MESSAGE = response.data;
		});
	})();

	$window.onfocus = () => {
		$scope.newMessageCount = 0;
		$scope.$apply();
	};

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
		if ($scope.isEditMessageView) {
			$scope.editMessage();
			return;
		}

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

	$scope.editMessage = () => {
		$scope.editingMessage.message.text = $scope.messageText;
		$http({
			url: API_URL + "/messaging/edit",
			method: "POST",
			data: $scope.editingMessage.message,
			headers: {
				"Auth-Token": $scope.token
			}
		}).then((response) => {
			$scope.updatePreviews();
			$scope.openConversation($scope.currentPreview);
			$scope.selectedMessages = [];
			$scope.isEditMessageView = false;
			$scope.messageText = '';
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

	$scope.editSelectedMessage = () => {
		$scope.editingMessage = $scope.selectedMessages[0];

		$scope.selectedMessages = [];
		$scope.isEditMessageView = true;

		$scope.messageText = $scope.editingMessage.message.text;
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
					console.log(action);

					if (action.type === "NEW_MESSAGE") {
						let messageReceived = action.message;
						messageReceived.mine = $scope.ME.user.login === messageReceived.sender.user.login;
						messageReceived.status = "received";

						if (!messageReceived.mine) {
							console.log($scope.notification);
							$scope.notification.play();

							console.log(document.visibilityState);
							if (document.visibilityState === 'hidden') {
								$scope.newMessageCount++;
							}
						}

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

	$scope.getMessageEdits = () => {
		if ($scope.listen) {
			$http({
				url: API_URL + "/messaging/get/e",
				method: "GET",
				headers: {
					"Auth-Token": $scope.token
				}
			}).then(
				(response) => {
					$scope.getMessageEdits();

					let action = response.data;
					console.log(action);

					if (action.type === "MESSAGE_EDIT") {
						let messageReceived = action.message;

						if ($scope.state === 'in' &&
							$scope.currentConversationId === messageReceived.conversation.id) {
							$scope.messages.find(m => m.message.id === messageReceived.message.id).message.text = messageReceived.message.text;
						}
					}

					// TODO: investigate
					$scope.updatePreviews();
					$scope.updatePreviews();
				},
				(error) => {
					$scope.getMessageEdits();
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