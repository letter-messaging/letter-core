import {Component, OnInit} from '@angular/core';
import {ConversationService} from "./conversation.service";
import {ActivatedRoute} from "@angular/router";
import {AppComponent} from "../app.component";
import {PollingService} from "../polling.service";
import {NotificationService} from "../notification.service";
import {MessengerComponent} from "../messenger/messenger.component";

@Component({
  selector: 'app-conversation',
  templateUrl: './conversation.component.html',
  styleUrls: ['./conversation.component.css']
})
export class ConversationComponent implements OnInit {

  public state = 'out';
  public id: number;
  public messages: Array<any>;

  constructor(private conversationService: ConversationService, private route: ActivatedRoute,
              private root: AppComponent, private pollingService: PollingService,
              private notificationService: NotificationService,
              private messengerComponent: MessengerComponent) {
    this.route.params.subscribe(p => this.id = p.id);
  }

  ngOnInit() {
  }

  getNewMessages() {
    if (this.root.listen) {
      this.pollingService.getMessages().subscribe((response: any) => {
          this.getNewMessages();

          let action = response.data;
          console.log(action);

          if (action.type === "NEW_MESSAGE") {
            let messageReceived = action.message;
            messageReceived.mine = this.root.ME.user.login === messageReceived.sender.user.login;
            messageReceived.status = "received";

            if (!messageReceived.mine) {
              this.notificationService.play();

              console.log(document.visibilityState);
              if (document.visibilityState === 'hidden') {
                this.notificationService.newMessageCount++;
              }
            }

            if (this.state === 'in' &&
              this.id === messageReceived.conversation.id) {
              this.messages.unshift(messageReceived);
            }
          }

          // TODO: investigate
          this.messengerComponent.updatePreviews();
          this.messengerComponent.updatePreviews();
        },
        (error) => {
          this.getNewMessages();
        });
    }
  }

  getMessageEdits() {
    if (this.root.listen) {
      this.pollingService.getEdits().subscribe((response: any) => {
          this.getMessageEdits();

          let action = response.data;
          console.log(action);

          if (action.type === "MESSAGE_EDIT") {
            let messageReceived = action.message;

            if (this.state === 'in' &&
              this.id === messageReceived.conversation.id) {
              this.messages
                .find(m => m.message.id === messageReceived.message.id).message.text = messageReceived.message.text;
            }
          }

          // TODO: investigate
          this.messengerComponent.updatePreviews();
          this.messengerComponent.updatePreviews();
        },
        (error) => {
          this.getMessageEdits();
        });
    }
  }

  getConversationRead() {
    if (this.root.listen) {
      this.pollingService.getReads().subscribe((response: any) => {
          this.getConversationRead();

          let action = response.data;
          console.log(action);

          if (action.type === "CONVERSATION_READ") {
            if (this.id === action.conversation.id) {
              for (let message of this.messages) {
                message.message.read = true;
              }
            }
          }

          // TODO: investigate
          this.messengerComponent.updatePreviews();
          this.messengerComponent.updatePreviews();
        },
        (error) => {
          this.getConversationRead();
        })
    }
  }

}
