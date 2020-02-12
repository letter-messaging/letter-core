import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {FormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {AutosizeModule} from 'ngx-autosize';
import {ArraySortPipeAsc, ArraySortPipeDesc} from './pipe/array-sort.pipe';
import {AuthComponent} from './component/routed/auth/auth.component';
import {RegisterComponent} from './component/routed/register/register.component';
import {ImageAttachmentComponent} from './component/embedded/attachment/image-attachment/image-attachment.component';
import {ProfileComponent} from './component/embedded/profile/profile.component';
import {ForwardedAttachmentComponent} from './component/embedded/attachment/forwarded-attachment/forwarded-attachment.component';
import {MessageComponent} from './component/embedded/message/message.component';
import {MessagingComponent} from './component/routed/messaging/messaging.component';
import {ConversationPreviewComponent} from './component/embedded/preview/conversation-preview/conversation-preview.component';
import {UserPreviewComponent} from './component/embedded/preview/user-preview/user-preview.component';
import {ProfileMenuComponent} from './component/embedded/menu/profile-menu/profile-menu.component';
import {AttachmentsMenuComponent} from './component/embedded/menu/attachments-menu/attachments-menu.component';
import {ConversationMenuComponent} from './component/embedded/menu/conversation-menu/conversation-menu.component';
import {MessageSendDirective} from './directive/message-send.directive';
import {ScrollPositionDirective} from './directive/scroll-position.directive';
import {DocumentAttachmentComponent} from './component/embedded/attachment/document-attachment/document-attachment.component';
import {ImagePreviewComponent} from './component/embedded/image-preview/image-preview.component';
import {ChatInfoComponent} from './component/embedded/chat-info/chat-info.component';

@NgModule({
    declarations: [
        AppComponent,
        AuthComponent,
        RegisterComponent,
        MessagingComponent,
        MessageComponent,
        MessageSendDirective,
        ScrollPositionDirective,
        ArraySortPipeAsc,
        ArraySortPipeDesc,
        ForwardedAttachmentComponent,
        ProfileComponent,
        ImageAttachmentComponent,
        ConversationPreviewComponent,
        UserPreviewComponent,
        ProfileMenuComponent,
        AttachmentsMenuComponent,
        ConversationMenuComponent,
        DocumentAttachmentComponent,
        ImagePreviewComponent,
        ChatInfoComponent,
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        HttpClientModule,
        AutosizeModule
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}
