import {Component, HostListener, OnInit} from '@angular/core';
import {Preview} from '../dto/Preview';
import {ActivatedRoute, Router} from '@angular/router';
import {PreviewService} from '../../service/preview.service';
import {MessengerService} from '../../service/messenger.service';
import {MessageService} from '../../service/message.service';
import {Message} from '../dto/Message';
import {User} from '../dto/User';
import {SearchService} from '../../service/search.service';
import {AuthService} from '../../service/auth.service';
import {CookieService} from '../../service/cookie.service';
import {MessagingService} from '../../service/messaging.service';
import {NewMessage} from '../dto/NewMessage';
import {ConversationService} from '../../service/conversation.service';

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
  private isPolling = false;

  me: User;

  previews: Array<Preview>;
  messages: Array<Message>;
  routeConversationId: number;
  currentPreview: Preview;

  messageText = '';

  searchText = '';
  searchUsers: Array<User> = [];
  searchPreviews: Array<Preview> = [];

  isSelectForwardTo = false;

  selectedMessages: Array<Message> = [];

  editingMessage: Message;

  isLeftView = true;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private previewService: PreviewService,
              private messengerService: MessengerService,
              private messageService: MessageService,
              private authService: AuthService,
              private searchService: SearchService,
              private cookieService: CookieService,
              private messagingService: MessagingService,
              private conversationService: ConversationService) {
  }

  @HostListener('document:keydown', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
    if (event.key === 'Escape') {
      this.closeConversation();
    }
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

        // makes it execute only once per application load
        if (!this.isPolling) {
          this.isPolling = true;
          this.startPolling();
        }

        this.previewService.all(this.token).subscribe(previews => {
          this.previews = previews.filter(p => p.lastMessage);

          this.routeConversationId = params['id'];
          if (this.routeConversationId) {
            this.isLeftView = false;
            this.messageService.get(this.token, this.routeConversationId, 0).subscribe(messages => {
              this.searchText = '';

              if (messages.length != 0) {
                this.currentPreview = this.previews.find(p => p.conversation.id == this.routeConversationId);
              } else {
                this.previewService.get(this.token, this.routeConversationId).subscribe(preview => {
                  this.currentPreview = preview;
                });
              }
              this.messages = messages;
            });
          } else {
            this.isLeftView = true;
            this.currentPreview = null;
            this.messages = [];
          }
        });
      });
    });
  }

  updatePreviews() {
    this.previewService.all(this.token).subscribe(previews => {
      this.previews = previews;
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
    this.router.navigate(['/im'], {queryParams: {id: conversationId}, replaceUrl: true});
  }

  closeConversation() {
    this.router.navigate(['/im'], {replaceUrl: true});
  }

  createConversation(user: User) {
    this.conversationService.create(this.token, user.login).subscribe(conversation => {
      this.router.navigate(['/im'], {queryParams: {id: conversation.id}, replaceUrl: true});
    });
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
    const message = new NewMessage();
    message.senderId = this.me.id;
    message.conversationId = this.currentPreview.conversation.id;
    message.text = this.messageText;
    message.forwarded = [];

    this.messageText = '';

    const tempViewMessage = new Message();
    tempViewMessage.sender = this.me;
    tempViewMessage.forwarded = message.forwarded;
    tempViewMessage.text = message.text;

    this.messages.unshift(tempViewMessage);

    this.messagingService.sendMessage(this.token, message).subscribe(m => {
      this.updatePreviews();
      this.messages = this.messages.filter(mes => mes.id);
      this.messages.unshift(m);
    });
  }

  selectMessage(message: Message) {
    if (!message.selected) {
      message.selected = true;
      this.selectedMessages.push(message);
    } else {
      message.selected = false;
      this.selectedMessages = this.selectedMessages.filter(m => m.id !== message.id);
    }
  }

  deselectMessages() {
    this.selectedMessages.forEach(m => m.selected = false);
    this.selectedMessages = [];
  }

  logout() {
    this.cookieService.deleteToken();
    this.router.navigate(['/auth'], {replaceUrl: true});
  }

  private startPolling() {
    console.log('start polling!');
    this.getMessage();
    this.getRead();
    this.getEdit();
  }

  private getMessage() {
    this.messagingService.getMessage(this.token).subscribe(
      newMessageAction => {
        this.getMessage();
        this.updatePreviews();
        if (newMessageAction.message.sender.id === this.me.id) {
          return;
        }

        if (this.currentPreview && newMessageAction.message.conversation.id === this.currentPreview.conversation.id) {
          this.messages.unshift(newMessageAction.message);
        }
      },
      error => {
        this.getMessage();
      }
    );
  }

  private getRead() {
    this.messagingService.getRead(this.token).subscribe(
      conversationReadAction => {
        console.log(conversationReadAction);
        this.getRead();
        this.updatePreviews();
        if (conversationReadAction.reader.id === this.me.id) {
          return;
        }

        if (this.currentPreview && conversationReadAction.conversation.id === this.currentPreview.conversation.id) {
          this.messages.forEach(m => m.read = true);
        }
      },
      error => {
        this.getRead();
      }
    );
  }

  private getEdit() {
    this.messagingService.getEdit(this.token).subscribe(
      messageEditAction => {
        this.getEdit();
        this.updatePreviews();

        if (this.currentPreview && messageEditAction.message.conversation.id === this.currentPreview.conversation.id) {
          this.messages.filter(m => m.id !== messageEditAction.message.id);
          this.messages.unshift(messageEditAction.message);
        }
      },
      error => {
        this.getEdit();
      }
    );
  }

}
