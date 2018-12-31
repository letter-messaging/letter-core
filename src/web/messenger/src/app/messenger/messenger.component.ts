import {Component, OnInit} from '@angular/core';
import {MessengerService} from "./messenger.service";

import * as moment_ from 'moment';
import {AppComponent} from "../app.component";

const moment = moment_;

@Component({
  selector: 'app-messenger',
  templateUrl: './messenger.component.html',
  styleUrls: ['./messenger.component.css']
})
export class MessengerComponent implements OnInit {

  public previews: Array<any>;

  constructor(private messengerService: MessengerService, private root: AppComponent) {
  }

  ngOnInit() {
  }

  updatePreviews() {
    this.messengerService.getAllPreviews().subscribe((response: any) => {
      response.data = response.data.filter((p) => p.lastMessage != null);
      response.data.sort((a, b) => moment(a.lastMessage.message.sent).isAfter(b.lastMessage.message.sent) ? -1 : 1);

      for (let preview of response.data) {
        if (preview.lastMessage) {
          preview.lastMessage.mine = preview.lastMessage.sender.user.login === this.root.ME.user.login;
        }
      }
      this.previews = response.data;
    });
  }

}
