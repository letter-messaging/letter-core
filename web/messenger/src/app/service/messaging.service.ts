import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {API_URL} from '../../../globals';
import {EditMessage} from '../dto/EditMessage';
import {Message} from '../dto/Message';
import {NewMessage} from '../dto/NewMessage';

// TODO: deal with 2s lag between EventSource reconnections
@Injectable({
	providedIn: 'root'
})
export class MessagingService {

	constructor(private http: HttpClient) {
	}

	getEvents(token: string): Observable<any> {
		return new Observable(o => {
			const es = new EventSource(API_URL + 'messaging/listen' + '?token=' + token);
			es.addEventListener('message', (e: any) => {
				o.next(JSON.parse(e.data));
			});
		});
	}

	sendMessage(token: string, newMessage: NewMessage): Observable<Message> {
		return this.http.post<Message>(API_URL + 'messaging/send', newMessage, {
			headers: {'Auth-Token': token}
		});
	}

	editMessage(token: string, editMessage: EditMessage): Observable<Message> {
		return this.http.post<Message>(API_URL + 'messaging/edit', editMessage, {
			headers: {'Auth-Token': token}
		});
	}

}
