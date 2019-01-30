import {Component, Input, OnInit} from '@angular/core';
import {Preview} from '../dto/Preview';
import {MessengerService} from '../../service/messenger.service';
import {User} from '../dto/User';
import {FILE_URL} from '../../../../globals';

@Component({
	selector: 'app-preview',
	templateUrl: './preview.component.html',
	styleUrls: ['./preview.component.scss']
})
export class PreviewComponent implements OnInit {

	readonly FILE_URL = FILE_URL;

	@Input()
	preview: Preview;

	@Input()
	selected: boolean;

	@Input()
	isOnline: boolean;

	me: User;

	constructor(private messengerService: MessengerService) {
	}

	ngOnInit() {
		this.messengerService.oMe.subscribe(me => this.me = me);
	}

}
