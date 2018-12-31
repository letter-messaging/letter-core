import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {API_URL} from "../../../globals";

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  constructor(private http: HttpClient) { }

  register(credentials) {
    return this.http.post(API_URL + '/register', credentials);
  }

}
