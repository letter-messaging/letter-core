import {Injectable} from '@angular/core';

@Injectable({
	providedIn: 'root'
})
export class ConfirmService {

	constructor() {
	}

	confirm(message: string): boolean {
		return window.confirm(message);
	}

}
