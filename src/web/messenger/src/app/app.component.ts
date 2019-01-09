import {Component} from '@angular/core';
import {CookieService} from './service/cookie.service';
import {AuthService} from './service/auth.service';
import {MessengerService} from './service/messenger.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  constructor(private cookieService: CookieService,
              private authService: AuthService,
              private messengerService: MessengerService,
              private router: Router,
              private route: ActivatedRoute) {
    this.autoLogin();
  }

  private autoLogin() {
    const token = this.cookieService.getToken();
    if (token !== null) {
      this.authService.validate(token).subscribe(
        user => {
          this.messengerService.setToken(token);
          this.messengerService.setMe(user);

          this.route.queryParams.subscribe(params => {
            if (params['id']) {
              this.router.navigate(['/im'], {queryParams: {id: params['id']}, replaceUrl: true});
            } else {
              this.router.navigate(['/im'], {replaceUrl: true});
            }
          });
        },
        error => {
          this.router.navigate(['/auth'], {replaceUrl: true});
        }
      );
    } else {
      this.router.navigate(['/auth'], {replaceUrl: true});
    }
  }

}
