import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {AppComponent} from '../../../../app.component';
import {TokenProvider} from '../../../../provider/token-provider';
import {ConversationService} from '../../../../service/conversation.service';
import {Preview} from '../../../../dto/Preview';
import {ConfirmService} from '../../../../service/confirm.service';

@Component({
    selector: 'app-conversation-menu',
    templateUrl: './conversation-menu.component.html',
    styleUrls: ['./conversation-menu.component.scss']
})
export class ConversationMenuComponent implements OnInit {

    @Input() currentPreview: Preview;
    @Output() closeConversation = new EventEmitter();

    visible = false;

    private token: string;

    constructor(
        private app: AppComponent,
        private tokenProvider: TokenProvider,
        private conversationService: ConversationService,
        private confirmService: ConfirmService
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
        if (this.confirmService.confirm('All conversation messages will be deleted')) {
            this.conversationService.delete(this.token, conversationId).subscribe(() => {
                this.visible = false;
                this.closeConversation.next();
            });
        }
    }

    hideConversation(conversationId: number) {
        this.conversationService.hide(this.token, conversationId).subscribe(() => {
            this.visible = false;
            this.closeConversation.next();
        });
    }

}
