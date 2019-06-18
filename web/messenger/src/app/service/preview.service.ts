import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {API_URL} from '../../../globals';
import {Preview} from '../dto/Preview';
import {Pageable} from '../dto/Pageable';

@Injectable({
	providedIn: 'root'
})
export class PreviewService {

	constructor(private http: HttpClient) {
	}

	all(token: string, pageable: Pageable): Observable<Preview[]> {
		return this.http.get<Preview[]>(API_URL + 'preview/all', {
			headers: {'Auth-Token': token},
			params: pageable.toHttpParams()
		});
	}

	get(token: string, conversationId: number): Observable<Preview> {
		return this.http.get<Preview>(API_URL + 'preview/get', {
			headers: {'Auth-Token': token},
			params: {'conversationId': conversationId.toString()}
		});
	}

}
