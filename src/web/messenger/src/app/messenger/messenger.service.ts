import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {API_URL} from "../../../globals";
import {AppComponent} from "../app.component";

@Injectable({
  providedIn: 'root'
})
export class MessengerService {

  constructor(private http: HttpClient, private root: AppComponent) {
  }

  getAllPreviews() {
    return this.http.get(API_URL + '/preview/all', {
      headers: {'Auth-Token': this.root.token}
    })
  }

}
