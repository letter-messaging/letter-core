import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {AuthComponent} from './component/auth/auth.component';
import {RegisterComponent} from './component/register/register.component';
import {MessagingComponent} from './component/messaging/messaging.component';
import {FormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {PreviewComponent} from './component/preview/preview.component';
import {MessageComponent} from './component/message/message.component';
import {AutosizeModule} from 'ngx-autosize';
import {MessageSendDirective} from './component/messaging/message-send.directive';
import {ArraySortPipeAsc, ArraySortPipeDesc} from './pipe/array-sort.pipe';
import {ForwardedAttachmentComponent} from './component/attachment/forwarded-attachment/forwarded-attachment.component';
import {ShowAttachmentsMenuDirective} from './component/messaging/show-attachments-menu.directive';
import {ScrollTopDirective} from './component/messaging/scroll-top.directive';
import {ProfileComponent} from './component/profile/profile.component';
import {OutsideClickDirective} from './component/messaging/outside-click.directive';
import {OverlayClickDirective} from './component/messaging/overlay-click.directive';
import { ImageAttachmentComponent } from './component/attachment/image-attachment/image-attachment.component';

@NgModule({
	declarations: [
		AppComponent,
		AuthComponent,
		RegisterComponent,
		MessagingComponent,
		PreviewComponent,
		MessageComponent,
		MessageSendDirective,
		ShowAttachmentsMenuDirective,
		ScrollTopDirective,
		OutsideClickDirective,
		OverlayClickDirective,
		ArraySortPipeAsc,
		ArraySortPipeDesc,
		ForwardedAttachmentComponent,
		ProfileComponent,
		ImageAttachmentComponent,
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
