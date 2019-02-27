import {Injectable} from '@angular/core';
import {API_URL} from '../../../globals';
import {HttpClient} from '@angular/common/http';
import {RegisterUser} from '../dto/RegisterUser';

@Injectable({
	providedIn: 'root'
})
export class RegisterService {

	constructor(private http: HttpClient) {
	}

	register(registerUser: RegisterUser) {
		return this.http.post(API_URL + 'register', registerUser);
	}

}
