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
    return this.http.get<Conversation>(API_URL + '/conversation/create', {
      headers: {'Auth-Token': token},
      params: {'withLogin': withLogin}
    });
  }

}
