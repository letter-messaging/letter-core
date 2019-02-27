import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {API_URL} from '../../../globals';
import {Preview} from '../dto/Preview';
import {User} from '../dto/User';

@Injectable({
	providedIn: 'root'
})
export class SearchService {

	constructor(private http: HttpClient) {
	}

	searchConversations(token: string, search: string): Observable<Array<Preview>> {
		return this.http.get<Array<Preview>>(API_URL + 'search/conversations', {
			headers: {'Auth-Token': token},
			params: {'search': search}
		});
	}

	searchUsers(token: string, search: string): Observable<Array<User>> {
		return this.http.get<Array<User>>(API_URL + 'search/users', {
			headers: {'Auth-Token': token},
			params: {'search': search}
		});
	}

}
