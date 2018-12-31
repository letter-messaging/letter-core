import { Injectable } from '@angular/core';
import {API_URL} from "../../globals";
import {HttpClient} from "@angular/common/http";
import {AppComponent} from "./app.component";

@Injectable({
  providedIn: 'root'
})
export class PollingService {

  constructor(private http: HttpClient, private root: AppComponent) { }

  getMessages() {
    return this.http.get(API_URL + '/messaging/get/m', {
      headers: {'Auth-Token': this.root.token}
    })
  }

  getEdits() {
    return this.http.get(API_URL + '/messaging/get/e', {
      headers: {'Auth-Token': this.root.token}
    })
  }

  getReads() {
    return this.http.get(API_URL + '/messaging/get/r', {
      headers: {'Auth-Token': this.root.token}
    })
  }

}
