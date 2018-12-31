import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {AuthComponent} from './auth/auth.component';
import {RegisterComponent} from './register/register.component';
import {MessengerComponent} from './messenger/messenger.component';
import {PreviewComponent} from './preview/preview.component';
import {MessageComponent} from './message/message.component';
import {ConversationComponent} from './conversation/conversation.component';
import {HttpClientModule} from "@angular/common/http";

@NgModule({
  declarations: [
    AppComponent,
    AuthComponent,
    RegisterComponent,
    MessengerComponent,
    PreviewComponent,
    MessageComponent,
    ConversationComponent

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
