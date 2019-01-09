import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Message} from '../../dto/Message';

@Component({
  selector: 'app-forwarded-attachment',
  templateUrl: './forwarded-attachment.component.html',
  styleUrls: [
    './forwarded-attachment.component.scss',
    './../attachment.scss',
  ]
})
export class ForwardedAttachmentComponent implements OnInit {

  @Input()
  forwarded: Array<Message>;

  @Output()
  removeForwardedAttachmentEvent = new EventEmitter();

  constructor() {
  }

  ngOnInit() {
  }

  removeForwardedAttachment() {
    this.removeForwardedAttachmentEvent.next();
  }

}
