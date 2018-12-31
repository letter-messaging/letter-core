import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthComponent} from "./auth/auth.component";
import {RegisterComponent} from "./register/register.component";
import {MessengerComponent} from "./messenger/messenger.component";
import {ConversationComponent} from "./conversation/conversation.component";

const routes: Routes = [
  {
    path: 'auth',
    component: AuthComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'messenger',
    component: MessengerComponent
  },
  {
    path: 'conversation/:id',
    component: ConversationComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
