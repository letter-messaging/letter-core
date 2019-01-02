import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Message} from '../component/dto/Message';
import {API_URL} from '../../../globals';
import {NewMessageAction} from '../component/dto/action/NewMessageAction';
import {ConversationReadAction} from '../component/dto/action/ConversationReadAction';
import {MessageEditAction} from '../component/dto/action/MessageEditAction';
import {NewMessage} from '../component/dto/NewMessage';
import {EditMessage} from '../component/dto/EditMessage';

@Injectable({
  providedIn: 'root'
})
export class MessagingService {

  constructor(private http: HttpClient) {
  }

  getMessage(token: string): Observable<NewMessageAction> {
    return this.http.get<NewMessageAction>(API_URL + '/messaging/get/m', {
      headers: {token: token},
    });
  }

  getRead(token: string): Observable<ConversationReadAction> {
    return this.http.get<ConversationReadAction>(API_URL + '/messaging/get/r', {
      headers: {token: token},
    });
  }

  getEdit(token: string): Observable<MessageEditAction> {
    return this.http.get<MessageEditAction>(API_URL + '/messaging/get/e', {
      headers: {token: token},
    });
  }

  sendMessage(token: string, newMessage: NewMessage): Observable<Message> {
    return this.http.post<Message>(API_URL + '/message/delete', newMessage, {
      headers: {'Auth-Token': token}
    });
  }

  editMessage(token: string, editMessage: EditMessage): Observable<Message> {
    return this.http.post<Message>(API_URL + '/message/edit', editMessage, {
      headers: {'Auth-Token': token}
    });
  }

}
