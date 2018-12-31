import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {API_URL} from "../../../globals";

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor(private http: HttpClient) {
  }

  getFullMessage() {
    return this.http.get(API_URL + '/message/init/full');
  }

}
