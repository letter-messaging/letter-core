import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Message} from '../../../dto/Message';
import {User} from '../../../dto/User';
import {API_URL, FILE_URL} from '../../../../../globals';
import {ImageService} from '../../../service/image.service';
import {ImageCompressionMode} from '../../../dto/enum/ImageCompressionMode';
import {Document} from '../../../dto/Document';


@Component({
	selector: 'app-message',
	templateUrl: './message.component.html',
	styleUrls: [
		'./message.component.scss',
		'./message.component.forwarded.scss'
	]
})
export class MessageComponent implements OnInit {

	readonly ImageCompressionMode: typeof ImageCompressionMode = ImageCompressionMode;
	readonly ImageService: typeof ImageService = ImageService;
	readonly FILE_URL = FILE_URL;

	@Input()
	message: Message;

	@Input()
	isForwarded: boolean;

	@Input()
	me: User;

	@Output()
	openProfileEvent = new EventEmitter<User>();

	mine: boolean;

	constructor() {
	}

	ngOnInit() {
		this.mine = this.message.sender.id === this.me.id;
	}

	openProfile(user: User): void {
		this.openProfileEvent.emit(user);
	}

	openDocument(document: Document) {
		window.open(FILE_URL + document.path, '_blank')
	}
}
