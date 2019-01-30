import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {API_URL} from '../../../globals';
import {Observable} from 'rxjs';
import {Avatar} from '../component/dto/Avatar';

@Injectable({
	providedIn: 'root'
})
export class AvatarService {

	constructor(private http: HttpClient) {
	}

	upload(token: string, file): Observable<Avatar> {
		const formData = new FormData();
		formData.append('avatar', file);

		return this.http.post<Avatar>(API_URL + 'avatar/upload', formData, {
			headers: {
				'Auth-Token': token
			}
		});
	}

}
