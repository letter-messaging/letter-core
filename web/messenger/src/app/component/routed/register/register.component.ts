import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {RegisterUser} from '../../../dto/RegisterUser';
import {RegisterService} from '../../../service/register.service';

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
