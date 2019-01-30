import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {API_URL} from '../../../globals';

@Injectable({
	providedIn: 'root'
})
export class AvatarService {

	constructor(private http: HttpClient) {
	}

	upload(token: string, file) {
		const formData = new FormData();
		formData.append('avatar', file);

		return this.http.post(API_URL + 'avatar/upload', formData, {
			headers: {
				'Auth-Token': token
			}
		});
	}

}
