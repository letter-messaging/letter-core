import {Injectable} from '@angular/core';

@Injectable({
	providedIn: 'root'
})
export class SoundNotificationService {

	private notificationAudio = new Audio('assets/sound/newmsg.mp3');

	constructor() {
	}

	notify() {
		this.notificationAudio.play();
	}

}
