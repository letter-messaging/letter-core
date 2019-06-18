import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FILE_URL} from '../../../../../../globals';
import {NewDocument} from '../../../../dto/NewDocument';

@Component({
	selector: 'app-document-attachment',
	templateUrl: './document-attachment.component.html',
	styleUrls: [
		'./document-attachment.component.scss',
		'./../attachment.scss',]
})
export class DocumentAttachmentComponent implements OnInit {

	readonly FILE_URL = FILE_URL;

	@Input() document: NewDocument;
	@Output() removeDocumentAttachment = new EventEmitter();

	constructor() {
	}

	ngOnInit() {
	}

	remove() {
		this.removeDocumentAttachment.next();
	}

}
