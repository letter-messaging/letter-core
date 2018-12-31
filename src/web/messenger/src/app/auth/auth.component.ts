import { Component, OnInit } from '@angular/core';
import {AppComponent} from "../app.component";
import {root} from "rxjs/internal-compatibility";

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent implements OnInit {

  constructor(private root: AppComponent) { }

  ngOnInit() {
  }

}
