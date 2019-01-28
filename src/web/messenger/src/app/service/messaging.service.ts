import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Message} from '../component/dto/Message';
import {API_URL} from '../../../globals';
import {NewMessage} from '../component/dto/NewMessage';
import {EditMessage} from '../component/dto/EditMessage';
import {ConversationReadAction} from '../component/dto/action/ConversationReadAction';
import {MessageEditAction} from '../component/dto/action/MessageEditAction';
import {NewMessageAction} from '../component/dto/action/NewMessageAction';

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
      return () => es.close();
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
