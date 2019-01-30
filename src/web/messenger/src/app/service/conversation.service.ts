import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {API_URL} from '../../../globals';
import {Conversation} from '../component/dto/Conversation';
import {Observable} from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class ConversationService {

	constructor(private http: HttpClient) {
	}

	create(token: string, withLogin: string): Observable<Conversation> {
		return this.http.get<Conversation>(API_URL + 'conversation/create', {
			headers: {'Auth-Token': token},
			params: {'with': withLogin}
		});
	}

	delete(token: string, conversationId: number) {
		return this.http.get(API_URL + 'conversation/delete', {
			headers: {'Auth-Token': token},
			params: {'id': conversationId.toString()}
		});
	}

	hide(token: string, conversationId: number) {
		return this.http.get(API_URL + 'conversation/hide', {
			headers: {'Auth-Token': token},
			params: {'id': conversationId.toString()}
		});
	}

}
