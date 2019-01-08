import {Directive, HostListener} from '@angular/core';
import {MessagingComponent} from './messaging.component';

@Directive({selector: '[appMessageSend]'})
export class MessageSendDirective {

  constructor(private messagingComponent: MessagingComponent) {
  }

  @HostListener('document:keypress', ['$event'])
  handleKeydown(event) {
    console.log(event);
    if (event.code === 'Enter' && !event.shiftKey) {
      this.messagingComponent.sendMessage();
      event.preventDefault();
    }
  }

}
