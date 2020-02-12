import {Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {AppComponent} from '../../../../app.component';
import {TokenProvider} from '../../../../provider/token-provider';
import {ImageService} from '../../../../service/image.service';
import {NewImage} from '../../../../dto/NewImage';
import {Message} from '../../../../dto/Message';
import {NewDocument} from '../../../../dto/NewDocument';
import {DocumentService} from '../../../../service/document.service';

@Component({
    selector: 'app-attachments-menu',
    templateUrl: './attachments-menu.component.html',
    styleUrls: ['./attachments-menu.component.scss']
})
export class AttachmentsMenuComponent implements OnInit {

    @Input() editingMessage: Message;
    @Output() attachedImages = new EventEmitter<NewImage>();
    @Output() attachedDocuments = new EventEmitter<NewDocument>();
    @ViewChild('imageInput') imageInput: ElementRef;
    @ViewChild('documentInput') documentInput: ElementRef;

    visible = false;

    private token: string;

    constructor(
        private app: AppComponent,
        private tokenProvider: TokenProvider,
        private imageService: ImageService,
        private documentService: DocumentService
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
        this.imageInput.nativeElement.click();
    }

    selectDocument() {
        this.documentInput.nativeElement.click();
    }

    onImageSelect() {
        this.visible = false;
        Array.from(this.imageInput.nativeElement.files).forEach(f =>
            this.imageService.upload(this.token, f).subscribe(newImage => {
                this.attachedImages.emit(newImage);
            }, err => {
            })
        );
    }

    onDocumentSelect() {
        this.visible = false;
        Array.from(this.documentInput.nativeElement.files).forEach(f =>
            this.documentService.upload(this.token, f).subscribe(newDocument => {
                this.attachedDocuments.emit(newDocument);
            }, err => {
            })
        );
    }
}
