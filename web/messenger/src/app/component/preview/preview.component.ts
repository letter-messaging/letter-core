import {Component, Input, OnInit} from '@angular/core';
import {FILE_URL} from '../../../../globals';
import {Preview} from '../../dto/Preview';
import {User} from '../../dto/User';
import {MeProvider} from '../../provider/me-provider';

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

	constructor(private meProvider: MeProvider) {
	}

	ngOnInit() {
		this.meProvider.oMe.subscribe(me => this.me = me);
	}

}
