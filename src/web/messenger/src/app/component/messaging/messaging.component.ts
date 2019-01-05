import {Component, OnInit} from '@angular/core';
import {Preview} from '../dto/Preview';
import {ActivatedRoute, Router} from '@angular/router';
import {PreviewService} from '../../service/preview.service';
import {MessengerService} from '../../service/messenger.service';
import {MessageService} from '../../service/message.service';
import {Message} from '../dto/Message';
import {User} from '../dto/User';

@Component({
  selector: 'app-messaging',
  templateUrl: './messaging.component.html',
  styleUrls: [
    './messaging.component.scss',
    './messaging.component.header-left.scss',
    './messaging.component.header-right.scss',
    './messaging.component.search.scss',
    './messaging.component.select-message.scss',
    './messaging.component.content-left.scss',
    './messaging.component.conversation.scss'
  ]
})
export class MessagingComponent implements OnInit {

  private token: string;

  me: User;

  previews: Array<Preview>;
  messages: Array<Message>;
  routeConversationId: number;
  currentPreview: Preview;

  messageText: string;
  searchText: string;

  isSelectForwardTo = false;

  selectedMessages: Array<Message> = [];

  isEditMessageView = false;

  editingMessage: Message;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private previewService: PreviewService,
              private messengerService: MessengerService,
              private messageService: MessageService) {
  }

  ngOnInit() {
    this.messengerService.oMe.subscribe(me => this.me = me);

    this.route.queryParams.subscribe(params => {
      this.messengerService.oToken.subscribe(token => {
        this.token = token;

        // initial page load issue
        if (this.token === '') {
          return;
        }

        this.previewService.all(this.token).subscribe(previews => {
          this.previews = previews.filter(p => p.lastMessage);

          this.routeConversationId = params['id'];
          if (this.routeConversationId) {
            this.messageService.get(this.token, this.routeConversationId, 0).subscribe(messages => {
              this.currentPreview = this.previews.find(p => p.conversation.id == this.routeConversationId);
              console.log(this.previews);
              this.messages = messages;
            });
          }
        });
      });
    });
  }

  openConversation(conversationId: number) {
    this.router.navigate(['/im'], {queryParams: {id: conversationId}});
  }

  searchForConversations() {

  }

  sendMessage() {
  }

  cancelEditing() {
  }
}
