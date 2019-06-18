import {Component, Input, OnInit} from '@angular/core';
import {User} from '../../../../dto/User';
import {ImageCompressionMode} from '../../../../dto/enum/ImageCompressionMode';
import {ImageService} from '../../../../service/image.service';
import {FILE_URL} from '../../../../../../globals';

@Component({
	selector: 'app-user-preview',
	templateUrl: './user-preview.component.html',
	styleUrls: ['./user-preview.component.scss']
})
export class UserPreviewComponent implements OnInit {

	readonly ImageCompressionMode: typeof ImageCompressionMode = ImageCompressionMode;
	readonly ImageService: typeof ImageService = ImageService;
	readonly FILE_URL = FILE_URL;

	@Input() user: User;

	constructor() {
	}

	ngOnInit() {
	}

}
