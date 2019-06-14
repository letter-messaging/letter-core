import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NewImage} from '../../../dto/NewImage';
import {FILE_URL} from '../../../../../globals';
import {ImageCompressionMode} from '../../../dto/enum/ImageCompressionMode';
import {ImageService} from '../../../service/image.service';

@Component({
	selector: 'app-image-attachment',
	templateUrl: './image-attachment.component.html',
	styleUrls: ['./image-attachment.component.scss',
		'./../attachment.scss',
	]
})
export class ImageAttachmentComponent implements OnInit {

	readonly ImageCompressionMode: typeof ImageCompressionMode = ImageCompressionMode;
	readonly ImageService: typeof ImageService = ImageService;
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
