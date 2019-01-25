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
    return this.http.get(API_URL + 'auth', {
      params: {'login': login, 'password': password},
      responseType: 'text'
    });
  }

  validate(token: string): Observable<User> {
    return this.http.get<User>(API_URL + 'auth/validate', {
      headers: {'Auth-Token': token}
    });
  }

  logout(token: string) {
    return this.http.get<User>(API_URL + 'auth/logout', {
      headers: {'Auth-Token': token}
    });
  }

}
