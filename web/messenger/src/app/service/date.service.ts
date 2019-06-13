import {Injectable} from '@angular/core';
import {MINUTES_AS_ONLINE_LIMIT} from '../../../globals';

import * as moment from 'moment';

@Injectable({
	providedIn: 'root'
})
export class DateService {

	constructor() {
	}

	static lastSeenView(lastSeen: Date): string {
		if (!lastSeen) {
			return 'Offline';
		}

		const now = moment();
		const time = moment(lastSeen);

		const minDiff = now.diff(time, 'minutes');
		if (minDiff < MINUTES_AS_ONLINE_LIMIT) {
			return 'Online';
		}
		if (minDiff < 60) {
			return 'Seen ' + minDiff + ' minutes ago';
		}
		const hourDiff = now.diff(time, 'hours');
		if (hourDiff < 24) {
			return 'Seen today at ' + time.format('hh:mm');
		}
		if (hourDiff < 48) {
			return 'Seen tomorrow at ' + time.format('hh:mm');
		}

		return time.format('[Seen ] MMM Do [ at ] hh:mm');
	}

}
