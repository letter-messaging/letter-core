import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {DateService} from '../../service/date.service';

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

  @Output() closeProfile = new EventEmitter();

  constructor() {
  }

  ngOnInit() {
  }

  lastSeenView(lastSeen: Date): string {
    return DateService.lastSeenView(lastSeen);
  }

  close() {
    this.closeProfile.emit();
  }

}
