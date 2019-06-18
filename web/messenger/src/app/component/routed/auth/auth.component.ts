import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from '../../../service/auth.service';
import {TokenProvider} from '../../../provider/token-provider';
import {MeProvider} from '../../../provider/me-provider';
import {CookieService} from '../../../service/cookie.service';

@Component({
	selector: 'app-auth',
	templateUrl: './auth.component.html',
	styleUrls: ['./auth.component.scss']
})
export class AuthComponent implements OnInit {

	credentials = {
		login: null,
		password: null
	};

	constructor(private authService: AuthService,
	            private tokenProvider: TokenProvider,
	            private meProvider: MeProvider,
	            private cookieService: CookieService,
	            private router: Router) {
	}

	ngOnInit() {
	}

	login() {
		this.authService.authenticate(this.credentials.login, this.credentials.password).subscribe(
			token => {
				this.credentials.password = null;

				this.tokenProvider.setToken(token);
				// TODO: refactor so tokenProvider deal with cookies
				this.cookieService.setToken(token);

				this.authService.validate(token).subscribe(user => {
					this.meProvider.setMe(user);
					this.router.navigate(['/im']);
				});

			},
			error => {
				this.credentials.password = null;
				return console.error(error);
			}
		);
	}

}
