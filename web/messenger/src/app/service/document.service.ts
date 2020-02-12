import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {NewImage} from '../dto/NewImage';
import {API_URL} from '../../../globals';
import {HttpClient} from '@angular/common/http';

@Injectable({
	providedIn: 'root'
})
export class DocumentService {

	constructor(private http: HttpClient) {
	}

	upload(token: string, file: File): Observable<NewImage> {
		const formData = new FormData();
		formData.append('document', file);

		return this.http.post<NewImage>(API_URL + 'document/upload', formData, {
			headers: {
				'Auth-Token': token
			}
		});
	}

}
