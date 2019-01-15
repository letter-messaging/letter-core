import {Directive, EventEmitter, HostListener, Output} from '@angular/core';

@Directive({selector: '[appScrollTop]'})
export class ScrollTopDirective {

  @Output('appScrollTop') scrollTop = new EventEmitter();

  constructor() {
  }

  @HostListener('scroll', ['$event'])
  handleScroll(event) {
    if (event.target.scrollTop === 0) {
      this.scrollTop.emit();
    }
  }

}
