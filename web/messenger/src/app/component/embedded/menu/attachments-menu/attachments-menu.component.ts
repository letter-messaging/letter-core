import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {AppComponent} from '../../../../app.component';
import {TokenProvider} from '../../../../provider/token-provider';
import {ImageService} from '../../../../service/image.service';
import {NewImage} from '../../../../dto/NewImage';
import {Message} from '../../../../dto/Message';

@Component({
	selector: 'app-attachments-menu',
	templateUrl: './attachments-menu.component.html',
	styleUrls: ['./attachments-menu.component.scss']
})
export class AttachmentsMenuComponent implements OnInit {

	@Input()
	editingMessage: Message;

	@Output()
	attachedImages = new EventEmitter<NewImage>();

	@ViewChild('fileInput') fileInput;

	visible = false;

	private token: string;

	constructor(
		private app: AppComponent,
		private tokenProvider: TokenProvider,
		private imageService: ImageService
	) {
	}

	ngOnInit() {
		this.app.onLoad(() => {
			this.tokenProvider.oToken.subscribe(token => {
				this.token = token;
			});
		});
	}

	selectImage() {
		this.fileInput.nativeElement.click();
	}

	onImageSelect() {
		this.visible = false;
		Array.from(this.fileInput.nativeElement.files).forEach(f =>
			this.imageService.upload(this.token, f).subscribe(newImage => {
				this.attachedImages.emit(newImage);
			}, err => {
			})
		);
	}
}
