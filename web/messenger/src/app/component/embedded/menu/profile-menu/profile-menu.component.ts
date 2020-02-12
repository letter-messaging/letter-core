import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {User} from '../../../../dto/User';
import {AppComponent} from '../../../../app.component';
import {MeProvider} from '../../../../provider/me-provider';
import {ImageCompressionMode} from '../../../../dto/enum/ImageCompressionMode';
import {ImageService} from '../../../../service/image.service';
import {FILE_URL} from '../../../../../../globals';
import {Preview} from '../../../../dto/Preview';
import {CookieService} from '../../../../service/cookie.service';
import {Router} from '@angular/router';

@Component({
    selector: 'app-profile-menu',
    templateUrl: './profile-menu.component.html',
    styleUrls: ['./profile-menu.component.scss']
})
export class ProfileMenuComponent implements OnInit {

    readonly ImageCompressionMode: typeof ImageCompressionMode = ImageCompressionMode;
    readonly ImageService: typeof ImageService = ImageService;
    readonly FILE_URL = FILE_URL;

    @Input() currentPreview: Preview;
    @Output() openProfile = new EventEmitter<User>();

    visible = false;
    me: User;

    constructor(
        private app: AppComponent,
        private meProvider: MeProvider,
        private cookieService: CookieService,
        private router: Router,
    ) {
    }

    ngOnInit() {
        this.app.onLoad(() => {
            this.meProvider.oMe.subscribe(me => {
                this.me = me;
            });
        });
    }

    logout() {
        this.cookieService.deleteToken();
        location.replace('/');
    }

    open(me: User) {
        this.visible = false;
        this.openProfile.next(me);
    }
}
