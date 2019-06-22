import {Component, EventEmitter, HostListener, Input, OnInit, Output} from '@angular/core';
import {Image} from '../../../dto/Image';
import {MessageImage} from '../../../dto/local/MessageImage';
import {ImageCompressionMode} from '../../../dto/enum/ImageCompressionMode';
import {ImageService} from '../../../service/image.service';
import {FILE_URL} from '../../../../../globals';
import * as moment from 'moment';

@Component({
	selector: 'app-image-preview',
	templateUrl: './image-preview.component.html',
	styleUrls: ['./image-preview.component.scss']
})
export class ImagePreviewComponent implements OnInit {

	readonly ImageCompressionMode: typeof ImageCompressionMode = ImageCompressionMode;
	readonly ImageService: typeof ImageService = ImageService;
	readonly FILE_URL = FILE_URL;

	@Input()
	messageImage: MessageImage;

	@Output()
	closeEvent = new EventEmitter();

	images: Image[];
	currentImageIndex: number;

	@HostListener('document:keydown', ['$event'])
	handleKeyboardEvent(event: KeyboardEvent) {
		console.debug(event);
		if (event.code === 'ArrowLeft') {
			this.openPrevious();
			return;
		}
		if (event.code === 'ArrowRight') {
			this.openNext();
			return;
		}
	}

	constructor() {
	}

	ngOnInit() {
		this.images = this.messageImage.message.images;
		this.currentImageIndex = this.images.findIndex(i => i.id === this.messageImage.image.id);
	}

	close() {
		this.closeEvent.emit();
	}

	openFullImage() {
		window.open(FILE_URL + this.images[this.currentImageIndex].path, '_blank');
	}

	formatTime(date: Date) {
		return moment(date).format('hh:mm');
	}

	deleteImage() {
		// TODO: image deletion
	}

	openPrevious() {
		if (this.currentImageIndex !== 0) {
			this.currentImageIndex--;
		}
	}

	openNext() {
		if (this.currentImageIndex !== this.images.length - 1) {
			this.currentImageIndex++;
		}
	}

}
