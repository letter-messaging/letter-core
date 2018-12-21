resize = (el) => {
	let textArea = el;
	textArea.style.height = '0px';
	textArea.style.height = textArea.scrollHeight - 8 + 'px';
};

document.querySelector('#send-message-text').addEventListener('keydown', function (e) {
		let textarea = document.getElementById("send-message-text");
		if (e.keyCode === 13) {
			if (e.ctrlKey) {
				textarea.value += "\n";
				resize(textarea);
			} else {
				e.preventDefault();
			}
		}
	}
);