import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BackgroundUnreadService {

  private unreadCount = new BehaviorSubject<number>(0);
  oUnreadCount = this.unreadCount.asObservable();

  constructor() {
  }

  incrementUnreadCount() {
    this.unreadCount.next(this.unreadCount.getValue() + 1);
  }

  resetUnreadCount() {
    this.unreadCount.next(0);
  }

}
