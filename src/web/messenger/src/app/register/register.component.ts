import {Component, OnInit} from '@angular/core';
import {RegisterService} from "./register.service";
import {AppComponent} from "../app.component";
import {root} from "rxjs/internal-compatibility";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  constructor(private registerService: RegisterService, private root: AppComponent) {
  }

  ngOnInit() {
  }

  register() {
    this.registerService.register(this.root.credentials).subscribe((response) => this.root.isLogin = true)
  }

}
