import {Component, Input, OnInit} from '@angular/core';
import {FILE_URL} from '../../../../globals';
import {Preview} from '../../dto/Preview';
import {User} from '../../dto/User';
import {MeProvider} from '../../provider/me-provider';
import {ImageCompressionMode} from '../../dto/enum/ImageCompressionMode';
import {ImageService} from '../../service/image.service';

@Component({
	selector: 'app-preview',
	templateUrl: './preview.component.html',
	styleUrls: ['./preview.component.scss']
})
export class PreviewComponent implements OnInit {

	readonly ImageCompressionMode: typeof ImageCompressionMode = ImageCompressionMode;
	readonly ImageService: typeof ImageService = ImageService;
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
