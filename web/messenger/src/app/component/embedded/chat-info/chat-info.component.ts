import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Conversation} from '../../../dto/Conversation';
import {ImageCompressionMode} from '../../../dto/enum/ImageCompressionMode';
import {ImageService} from '../../../service/image.service';
import {FILE_URL} from '../../../../../globals';
import {PreviewType} from '../../../dto/enum/PreviewType';
import {User} from '../../../dto/User';

@Component({
	selector: 'app-chat-info',
	templateUrl: './chat-info.component.html',
	styleUrls: [
		'./chat-info.component.scss',
		'./../profile/profile.component.scss',
		'./../preview/user-preview/user-preview.component.scss'
	]
})
export class ChatInfoComponent implements OnInit {

	readonly ImageCompressionMode: typeof ImageCompressionMode = ImageCompressionMode;
	readonly ImageService: typeof ImageService = ImageService;
	readonly FILE_URL = FILE_URL;
	readonly PreviewType: typeof PreviewType = PreviewType;

	@Input()
	chat: Conversation;

	@Output()
	close = new EventEmitter();

	@Output()
	openProfile = new EventEmitter<User>();

	constructor() {
	}

	ngOnInit() {
	}

}
