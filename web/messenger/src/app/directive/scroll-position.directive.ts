import {Directive, EventEmitter, HostListener, Output} from '@angular/core';

@Directive({selector: '[appScroll]'})
export class ScrollPositionDirective {

    @Output('appScrollTop') scrollTop = new EventEmitter();
    @Output('appScrollBottom') scrollBottom = new EventEmitter();

    constructor() {
    }

    @HostListener('scroll', ['$event'])
    handleScroll(event) {
        if (event.target.scrollTop === 0) {
            this.scrollTop.emit();
        }
        if (event.target.scrollTop === event.target.scrollHeight - event.target.clientHeight) {
            this.scrollBottom.emit();
        }
    }

}
