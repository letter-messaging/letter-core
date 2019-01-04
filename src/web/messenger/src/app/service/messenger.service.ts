import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';
import {User} from '../component/dto/User';

@Injectable({
  providedIn: 'root'
})
export class MessengerService {

  private me = new Subject<User>();
  oMe = this.me.asObservable();

  private token = new Subject<string>();
  oToken = this.token.asObservable();

  constructor() {
  }

  setMe(me: User) {
    this.me.next(me);
  }

  setToken(token: string) {
    this.token.next(token);
  }

}
