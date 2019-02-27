import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {API_URL} from '../../../globals';
import {NewImage} from '../dto/NewImage';

@Injectable({
	providedIn: 'root'
})
export class ImageService {

	constructor(private http: HttpClient) {
	}

	upload(token: string, file): Observable<NewImage> {
		const formData = new FormData();
		formData.append('image', file);

		return this.http.post<NewImage>(API_URL + 'image/upload', formData, {
			headers: {
				'Auth-Token': token
			}
		});
	}

}
