import {Directive, ElementRef, EventEmitter, HostListener, Output} from '@angular/core';

@Directive({
	selector: '[appOverlayClick]',
})
export class OverlayClickDirective {

	constructor(private _elementRef: ElementRef) {
	}

	@Output('appOverlayClick') clickOutside: EventEmitter<any> = new EventEmitter();

	@HostListener('document:click', ['$event.target']) onMouseEnter(targetElement) {
		if (targetElement.className === 'overlay') this.clickOutside.emit(null);
	}

}
