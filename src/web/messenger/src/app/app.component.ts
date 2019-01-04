import {Component} from '@angular/core';
import {CookieService} from './service/cookie.service';
import {AuthService} from './service/auth.service';
import {MessengerService} from './service/messenger.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  constructor(private cookieService: CookieService,
              private authService: AuthService,
              private messengerService: MessengerService,
              private router: Router) {
    this.autoLogin();
  }

  private autoLogin() {
    const token = this.cookieService.getToken();
    if (token !== null) {
      this.authService.validate(token).subscribe(
        user => {
          this.messengerService.setMe(user);
          this.router.navigate(['/im']);
        },
        error => {
          this.router.navigate(['/auth']);
        }
      );
    } else {
      this.router.navigate(['/auth']);
    }
  }
}
