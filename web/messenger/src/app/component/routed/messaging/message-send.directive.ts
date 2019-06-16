import {Directive, HostListener} from '@angular/core';
import {MessagingComponent} from './messaging.component';

@Directive({selector: '[appMessageSend]'})
export class MessageSendDirective {

	constructor(private messagingComponent: MessagingComponent) {
	}

	@HostListener('document:keypress', ['$event'])
	handleKeydown(event) {
		if (event.code === 'Enter' && !event.shiftKey) {
			if (this.messagingComponent.editingMessage) {
				this.messagingComponent.editMessage();
			} else {
				this.messagingComponent.sendMessage();
			}
			event.preventDefault();
		}
	}

}
