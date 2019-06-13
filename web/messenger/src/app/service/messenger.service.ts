import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {User} from '../dto/User';

@Injectable({
	providedIn: 'root'
})
export class MessengerService {

	private me = new BehaviorSubject<User>(null);
	oMe = this.me.asObservable();

	private token = new BehaviorSubject<string>('');
	oToken = this.token.asObservable();

	constructor() {
	}

	setMe(me: User) {
		this.me.next(me);
	}

	setToken(token: string) {
		this.token.next(token);
	}

}
