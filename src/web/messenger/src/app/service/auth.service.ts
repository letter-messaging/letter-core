import {Injectable} from '@angular/core';
import {API_URL} from '../../../globals';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../component/dto/User';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {
  }

  authenticate(login: string, password: string): Observable<string> {
    return this.http.get<string>(API_URL + '/auth/login', {
      params: {'login': login, 'password': password}
    });
  }

  validate(token: string): Observable<User> {
    return this.http.get<User>(API_URL + '/auth/validate', {
      headers: {'Auth-Token': token}
    });
  }
}
