import {Injectable} from '@angular/core';

@Injectable({
	providedIn: 'root'
})
export class SoundNotificationService {


	constructor() {
	}

	notify() {
		const notificationAudio = new Audio('assets/sound/newmsg.mp3');
		console.log('audio play', notificationAudio.readyState);
		let promise = notificationAudio.play();

		// required due to browser policy
		// described here: https://developers.google.com/web/updates/2017/09/autoplay-policy-changes
		if (promise) {
			promise.catch(e => {
			});
		}
	}

}
