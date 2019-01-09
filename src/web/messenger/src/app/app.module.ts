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
import {MessageSendDirective} from './component/messaging/MessageSendDirective';
import {ArraySortPipeAsc, ArraySortPipeDesc} from './pipe/ArraySortPipe';
import {ForwardedAttachmentComponent} from './component/attachment/forwarded-attachment/forwarded-attachment.component';

@NgModule({
  declarations: [
    AppComponent,
    AuthComponent,
    RegisterComponent,
    MessagingComponent,
    PreviewComponent,
    MessageComponent,
    MessageSendDirective,
    ArraySortPipeAsc,
    ArraySortPipeDesc,
    ForwardedAttachmentComponent,
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
