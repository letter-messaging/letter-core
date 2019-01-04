import {Component, OnInit} from '@angular/core';
import {Preview} from '../dto/Preview';
import {ActivatedRoute} from '@angular/router';
import {PreviewService} from '../../service/preview.service';
import {MessengerService} from '../../service/messenger.service';
import {MessageService} from '../../service/message.service';
import {Message} from '../dto/Message';

@Component({
  selector: 'app-messaging',
  templateUrl: './messaging.component.html',
  styleUrls: ['./messaging.component.scss']
})
export class MessagingComponent implements OnInit {

  previews: Array<Preview>;
  messages: Array<Message>;
  routeConversationId: number;
  currentPreview: Preview;

  messageText: string;
  searchText: string;

  isSelectForwardTo = false;

  selectedMessages: Array<Message> = [];

  isEditMessageView = false;

  constructor(private route: ActivatedRoute,
              private previewService: PreviewService,
              private messengerService: MessengerService,
              private messageService: MessageService) {
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.messengerService.oToken.subscribe(token => {
        this.previewService.all(token).subscribe(previews => {
          console.log(previews);
          return this.previews = previews;
        });

        this.routeConversationId = params['id'];
        if (this.routeConversationId) {
          this.openConversation(token, this.routeConversationId);
        }
      });
    });
  }

  openConversation(token, conversationId: number) {
    this.messageService.get(token, conversationId, 0).subscribe(messages => {
      this.currentPreview = this.previews.find(p => p.conversation.id === conversationId);
      this.messages = messages;
      console.log(this.messages);
    });
  }

  searchForConversations() {

  }

  sendMessage() {
  }

}
