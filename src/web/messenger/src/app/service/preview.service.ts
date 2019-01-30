import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {API_URL} from '../../../globals';
import {Preview} from '../component/dto/Preview';

@Injectable({
	providedIn: 'root'
})
export class PreviewService {

	constructor(private http: HttpClient) {
	}

	all(token: string): Observable<Array<Preview>> {
		return this.http.get<Array<Preview>>(API_URL + 'preview/all', {
			headers: {'Auth-Token': token},
		});
	}

	get(token: string, conversationId: number): Observable<Preview> {
		return this.http.get<Preview>(API_URL + 'preview/get', {
			headers: {'Auth-Token': token},
			params: {'conversationId': conversationId.toString()}
		});
	}

}
