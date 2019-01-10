import {Directive, HostListener} from '@angular/core';
import {MessagingComponent} from './messaging.component';
import {HOVER_TIME} from '../../../../globals';

@Directive({
  selector: '[appShowAttachmentsMenu]',
})
export class ShowAttachmentsMenuDirective {

  private timeout;

  constructor(private messagingComponent: MessagingComponent) {
  }

  @HostListener('mouseover', ['$event'])
  handleMouseover(event) {
    this.messagingComponent.showAttachmentsMenu = true;
    this.cancelClosing();
  }

  @HostListener('mouseout', ['$event'])
  handleMouseout(event) {
    this.close();
  }

  private close() {
    this.timeout = setTimeout(() => this.messagingComponent.showAttachmentsMenu = false, HOVER_TIME);
  }

  private cancelClosing() {
    clearTimeout(this.timeout);
  }

}
