import {Component, Input, OnInit} from '@angular/core';
import {Message} from '../dto/Message';
import {MessengerService} from '../../service/messenger.service';
import {User} from '../dto/User';


@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.scss']
})
export class MessageComponent implements OnInit {

  @Input()
  message: Message;

  @Input()
  isForwarded: boolean;

  me: User;

  constructor(private messengerService: MessengerService) {
  }

  ngOnInit() {
    this.messengerService.oMe.subscribe(me => this.me = me);
  }

}
