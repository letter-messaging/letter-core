import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {API_URL} from "../../../globals";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {
  }

  authenticate(login, password) {
    return this.http.get(API_URL + '/auth/login', {
      params: {login: login, password: password}
    })
  }

  validate(token) {
    return this.http.get(API_URL + '/auth/validate', {
      headers: {'Auth-Token': token}
    })
  }

}
