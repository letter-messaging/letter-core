import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  public notification = new Audio('/sound/newmsg.mp3');
  public newMessageCount = 0;

  constructor() {
  }

  play() {
    this.notification.play();
  }

}
