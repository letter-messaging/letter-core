import {Component, OnInit} from '@angular/core';
import {Preview} from '../dto/Preview';
import {ActivatedRoute, Router} from '@angular/router';
import {PreviewService} from '../../service/preview.service';
import {MessengerService} from '../../service/messenger.service';
import {MessageService} from '../../service/message.service';
import {Message} from '../dto/Message';
import {User} from '../dto/User';
import {SearchService} from '../../service/search.service';

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

  searchText: string = '';
  searchUsers: Array<User> = [];
  searchPreviews: Array<Preview> = [];

  isSelectForwardTo = false;

  selectedMessages: Array<Message> = [];

  isEditMessageView = false;

  editingMessage: Message;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private previewService: PreviewService,
              private messengerService: MessengerService,
              private messageService: MessageService,
              private searchService: SearchService) {
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
              this.searchText = '';

              this.currentPreview = this.previews.find(p => p.conversation.id == this.routeConversationId);
              this.messages = messages;
            });
          }
        });
      });
    });
  }

  // TODO: add debounce support
  previewsOrSearchPreviews(): Array<Preview> {
    if (this.searchText === '') {
      return this.previews;
    } else {
      return this.searchPreviews;
    }
  }

  openConversation(conversationId: number) {
    this.router.navigate(['/im'], {queryParams: {id: conversationId}});
  }

  searchForConversationsOrUsers() {
    if (this.searchText === '') {
      return;
    }

    if (this.searchText[0] === '@') {
      this.searchService.searchUsers(this.token, this.searchText)
        .subscribe(users => this.searchUsers = users);
    } else {
      this.searchService.searchConversations(this.token, this.searchText)
        .subscribe(conversations => this.searchPreviews = conversations);
    }
  }

  sendMessage() {
  }

  cancelEditing() {
  }

  createConversation(user: User) {
  }

}
