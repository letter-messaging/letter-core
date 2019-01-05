import {Component, Input, OnInit} from '@angular/core';
import {Message} from '../dto/Message';
import {User} from '../dto/User';


@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: [
    './message.component.scss',
    './message.component.forwarded.scss'
  ]
})
export class MessageComponent implements OnInit {

  @Input()
  message: Message;

  @Input()
  isForwarded: boolean;

  @Input()
  me: User;

  mine: boolean;

  constructor() {
  }

  ngOnInit() {
    this.mine = this.message.sender.id === this.me.id;
  }

}
