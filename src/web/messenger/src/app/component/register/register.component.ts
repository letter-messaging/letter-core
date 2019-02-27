import {Component, OnInit} from '@angular/core';
import {RegisterService} from '../../service/register.service';
import {Router} from '@angular/router';
import {RegisterUser} from '../../dto/RegisterUser';

@Component({
	selector: 'app-register',
	templateUrl: './register.component.html',
	styleUrls: ['./register.component.scss', './../auth/auth.component.scss']
})
export class RegisterComponent implements OnInit {

	registerUser: RegisterUser = new RegisterUser();
	passwordConfirmation = '';

	constructor(private router: Router,
	            private registerService: RegisterService) {
	}

	ngOnInit() {
	}

	register() {
		if (this.registerUser.password === this.passwordConfirmation) {
			this.registerService.register(this.registerUser).subscribe(success => {
				this.router.navigate(['/auth'], {replaceUrl: true});
			}, error => {
			});
		}
	}
}
