import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {User} from '../../../../dto/User';
import {ImageCompressionMode} from '../../../../dto/enum/ImageCompressionMode';
import {ImageService} from '../../../../service/image.service';
import {FILE_URL} from '../../../../../../globals';
import {Preview} from '../../../../dto/Preview';
import {PreviewType} from '../../../../dto/enum/PreviewType';

@Component({
    selector: 'app-user-preview',
    templateUrl: './user-preview.component.html',
    styleUrls: ['./user-preview.component.scss']
})
export class UserPreviewComponent implements OnInit {

    readonly ImageCompressionMode: typeof ImageCompressionMode = ImageCompressionMode;
    readonly ImageService: typeof ImageService = ImageService;
    readonly FILE_URL = FILE_URL;
    readonly PreviewType: typeof PreviewType = PreviewType;

    @Input() user: User;
    @Input() currentPreview: Preview;
    @Output() addToChatEvent = new EventEmitter<User>();

    constructor() {
    }

    ngOnInit() {
    }

}
