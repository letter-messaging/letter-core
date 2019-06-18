import {Component} from '@angular/core';
import {CookieService} from './service/cookie.service';
import {AuthService} from './service/auth.service';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {TokenProvider} from './provider/token-provider';
import {MeProvider} from './provider/me-provider';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.scss']
})
export class AppComponent {

	isLoaded: boolean = false;

	constructor(private cookieService: CookieService,
	            private authService: AuthService,
	            private tokenProvider: TokenProvider,
	            private meProvider: MeProvider,
	            private router: Router,
	            private route: ActivatedRoute) {
		this.autoLogin();
	}

	private autoLogin() {
		this.router.events.subscribe(
			(event: any) => {
				if (event instanceof NavigationEnd) {
					if (this.router.url === '/auth') {
						return;
					}

					const token = this.cookieService.getToken();
					if (token !== null) {
						this.authService.validate(token).subscribe(
							user => {
								this.tokenProvider.setToken(token);
								this.meProvider.setMe(user);
								this.isLoaded = true;

								this.route.queryParams.subscribe(params => {
									if (params['id']) {
										this.router.navigate(['/im'], {queryParams: {id: params['id']}, replaceUrl: true});
									} else {
										this.router.navigate(['/im']);
									}
								});
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
		);
	}

	onLoad(loaded: () => void) {
		setTimeout(() => {
			if (this.isLoaded) {
				loaded();
			} else {
				this.onLoad(loaded);
			}
		}, 10);
	}

}
