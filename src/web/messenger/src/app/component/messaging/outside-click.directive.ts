import {Directive, ElementRef, EventEmitter, HostListener, Output} from '@angular/core';

@Directive({
  selector: '[appOutsideClick]',
})
export class OutsideClickDirective {

  constructor(private _elementRef: ElementRef) {
  }

  @Output('appOutsideClick') clickOutside: EventEmitter<any> = new EventEmitter();

  @HostListener('document:click', ['$event.target']) onMouseEnter(targetElement) {
    if (targetElement.className === 'overlay') {
      this.clickOutside.emit(null);
    }
  }


}
