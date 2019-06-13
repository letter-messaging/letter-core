import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {User} from '../dto/User';

@Injectable({
	providedIn: 'root'
})
export class MeProvider {

	private me = new BehaviorSubject<User>(null);
	oMe = this.me.asObservable();

	constructor() {
	}

	setMe(me: User) {
		this.me.next(me);
	}

}
