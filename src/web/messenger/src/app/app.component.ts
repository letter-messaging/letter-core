import {Component, OnInit} from '@angular/core';
import {API_URL, LOCAL_STORAGE_TOKEN_NAME} from "../../globals";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "./auth/auth.service";
import {MessengerComponent} from "./messenger/messenger.component";
import {PollingService} from "./polling.service";
import {ConversationComponent} from "./conversation/conversation.component";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  public token;
  public ME;
  public credentials = {
    "login": null,
    "password": null,
    "confirmPassword": null,
    "firstName": null,
    "lastName": null
  };
  public isAuth = true;
  public isLoaded = false;
  public listen = false;
  public isLogin = true;

  constructor(private http: HttpClient,
              private authService: AuthService,
              private messengerComponent: MessengerComponent,
              private conversationComponent: ConversationComponent) {
  }

  ngOnInit(): void {
    this.autoLogin();
  }

  autoLogin() {
    if (localStorage.getItem(LOCAL_STORAGE_TOKEN_NAME)) {
      const unverifiedToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_NAME);

      this.authService.validate(unverifiedToken).subscribe((response: any) => {
        this.credentials.login = response.data.login;

        this.token = unverifiedToken;
        this.initialize();
        this.isAuth = false;
      }, (error) => {
        this.isLoaded = true;
      })
    } else {
      this.isLoaded = true;
    }
  }

  initialize() {
    this.authService.validate(this.token).subscribe((response: any) => {
      this.ME = response.data;
    });

    this.messengerComponent.updatePreviews();

    this.listen = true;
    this.conversationComponent.getNewMessages();
    this.conversationComponent.getMessageEdits();
    this.conversationComponent.getConversationRead();

    this.isLoaded = true;
  }

}
