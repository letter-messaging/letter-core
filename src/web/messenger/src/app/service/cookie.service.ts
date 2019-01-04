import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CookieService {

  LOCAL_STORAGE_TOKEN_NAME = 'Auth-Token';

  constructor() {
  }

  getToken(): string {
    return localStorage.getItem(this.LOCAL_STORAGE_TOKEN_NAME);
  }

  setToken(token: string) {
    localStorage.setItem(this.LOCAL_STORAGE_TOKEN_NAME, token);
  }

}
