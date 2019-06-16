import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {AppComponent} from '../../../../app.component';
import {TokenProvider} from '../../../../provider/token-provider';
import {ConversationService} from '../../../../service/conversation.service';
import {Preview} from '../../../../dto/Preview';

@Component({
	selector: 'app-conversation-menu',
	templateUrl: './conversation-menu.component.html',
	styleUrls: ['./conversation-menu.component.scss']
})
export class ConversationMenuComponent implements OnInit {

	@Input()
	currentPreview: Preview;

	@Output()
	closeConversation = new EventEmitter();

	conversationMenuView = false;

	private token: string;

	constructor(
		private app: AppComponent,
		private tokenProvider: TokenProvider,
		private conversationService: ConversationService
	) {
	}

	ngOnInit() {
		this.app.onLoad(() => {
			this.tokenProvider.oToken.subscribe(token => {
				this.token = token;
			});
		});
	}

	deleteConversation(conversationId: number) {
		this.conversationService.delete(this.token, conversationId).subscribe(success => {
			this.closeConversation.next();
		});
	}

	hideConversation(conversationId: number) {
		this.conversationService.hide(this.token, conversationId).subscribe(success => {
			this.closeConversation.next();
		});
	}

}
