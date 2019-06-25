import {Component, EventEmitter, HostListener, Input, OnInit, Output} from '@angular/core';
import {Image} from '../../../dto/Image';
import {MessageImage} from '../../../dto/local/MessageImage';
import {ImageCompressionMode} from '../../../dto/enum/ImageCompressionMode';
import {ImageService} from '../../../service/image.service';
import {FILE_URL} from '../../../../../globals';
import * as moment from 'moment';
import {ConfirmService} from '../../../service/confirm.service';
import {MeProvider} from '../../../provider/me-provider';
import {AppComponent} from '../../../app.component';
import {User} from '../../../dto/User';

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
	deleteImageEvent = new EventEmitter();

	images: Image[];
	currentImageIndex: number;
	me: User;

	@HostListener('document:keydown', ['$event'])
	handleKeyboardEvent(event: KeyboardEvent) {
		if (event.code === 'ArrowLeft') {
			this.openPrevious();
			return;
		}
		if (event.code === 'ArrowRight') {
			this.openNext();
			return;
		}
	}

	constructor(
		private app: AppComponent,
		private meProvider: MeProvider,
		private confirmService: ConfirmService,
	) {
	}

	ngOnInit() {
		this.app.onLoad(() => {
			this.meProvider.oMe.subscribe(me => {
				this.me = me;
			});
		});
		this.images = this.messageImage.message.images;
		this.currentImageIndex = this.images.findIndex(i => i.id === this.messageImage.image.id);
	}

	openFullImage() {
		window.open(FILE_URL + this.images[this.currentImageIndex].path, '_blank');
	}

	formatTime(date: Date) {
		return moment(date).format('hh:mm');
	}

	deleteImage() {
		if (this.confirmService.confirm('This image will be deleted')) {
			this.deleteImageEvent.emit();
		}
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
