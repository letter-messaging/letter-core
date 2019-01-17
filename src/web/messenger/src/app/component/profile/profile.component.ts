import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {DateService} from '../../service/date.service';
import {User} from '../dto/User';
import {UserInfo} from '../dto/UserInfo';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: [
    './profile.component.scss',
    './../messaging/messaging.component.search.scss'
  ]
})
export class ProfileComponent implements OnInit {

  @Input() currentProfile;

  @Input() me: User;

  @Output() closeProfile = new EventEmitter();

  @Output() editProfile = new EventEmitter<UserInfo>();

  editable = false;

  editView = false;

  constructor() {
  }

  ngOnInit() {
    this.editable = this.me.login === this.currentProfile.user.login;
  }

  lastSeenView(lastSeen: Date): string {
    return DateService.lastSeenView(lastSeen);
  }

  close() {
    this.closeProfile.emit();
  }

  edit() {
    console.log(this.currentProfile.userInfo.city);
    this.editProfile.emit(this.currentProfile.userInfo);
    this.editView = false;
  }

}
