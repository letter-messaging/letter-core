import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NewImage} from '../../../dto/NewImage';
import {FILE_URL} from '../../../../../globals';

@Component({
	selector: 'app-image-attachment',
	templateUrl: './image-attachment.component.html',
	styleUrls: ['./image-attachment.component.scss',
		'./../attachment.scss',
	]
})
export class ImageAttachmentComponent implements OnInit {

	readonly FILE_URL = FILE_URL;

	@Input()
	image: NewImage;

	@Output()
	removeImageAttachmentEvent = new EventEmitter();

	constructor() {
	}

	ngOnInit() {
	}

	removeImageAttachment() {
		this.removeImageAttachmentEvent.next();
	}

}
