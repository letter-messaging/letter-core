import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {API_URL} from '../../../globals';
import {Message} from '../dto/Message';
import {Pageable} from '../dto/Pageable';

@Injectable({
	providedIn: 'root'
})
export class MessageService {

	constructor(private http: HttpClient) {
	}

	get(token: string, conversationId: number, pageable: Pageable): Observable<Message[]> {
		return this.http.get<Message[]>(API_URL + 'message/get', {
			headers: {'Auth-Token': token},
			params: pageable.toHttpParams()
				.append('conversationId', conversationId.toString())
		});
	}

	delete(token: string, deleteMessages: Message[]) {
		return this.http.post(API_URL + 'message/delete', deleteMessages, {
			headers: {'Auth-Token': token}
		});
	}

}
