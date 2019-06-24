import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {API_URL} from '../../../globals';
import {NewChat} from '../dto/NewChat';
import {Conversation} from '../dto/Conversation';

@Injectable({
	providedIn: 'root'
})
export class ChatService {

	constructor(private http: HttpClient) {
	}

	create(token: string, newChat: NewChat): Observable<Conversation> {
		return this.http.post<Conversation>(API_URL + 'chat/create', newChat, {
			headers: {
				'Auth-Token': token
			}
		});
	}

	addMember(token: string, chatId: number, memberId: number): Observable<Conversation> {
		return this.http.get<Conversation>(API_URL + 'chat/add', {
			headers: {
				'Auth-Token': token
			},
			params: {
				'chatId': chatId.toString(),
				'memberId': memberId.toString()
			}
		});
	}

	addMembers(token: string, chatId: number, memberIds: number[]) {
		return this.http.post<Conversation>(API_URL + 'chat/add', memberIds, {
			headers: {
				'Auth-Token': token
			},
			params: {
				'chatId': chatId.toString(),
			}
		});
	}

	delete(token: string, conversationId: number) {
		return this.http.get(API_URL + 'chat/delete', {
			headers: {
				'Auth-Token': token
			},
			params: {
				'id': conversationId.toString()
			}
		});
	}

	hide(token: string, conversationId: number) {
		return this.http.get(API_URL + 'chat/hide', {
			headers: {
				'Auth-Token': token
			},
			params: {
				'id': conversationId.toString()
			}
		});
	}

}
