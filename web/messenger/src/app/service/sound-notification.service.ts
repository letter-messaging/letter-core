import {Injectable} from '@angular/core';

@Injectable({
	providedIn: 'root'
})
export class SoundNotificationService {

	private notificationAudio = new Audio('assets/sound/newmsg.mp3');

	constructor() {
		this.notificationAudio.load();
	}

	notify() {
		let promise = this.notificationAudio.play();

		// required due to browser policy
		// described here: https://developers.google.com/web/updates/2017/09/autoplay-policy-changes
		if (promise) {
			promise.catch(e => {
			});
		}
	}

}
