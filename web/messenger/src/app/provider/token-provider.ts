import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class TokenProvider {

    private token = new BehaviorSubject<string>('');
    oToken = this.token.asObservable();

    constructor() {
    }

    setToken(token: string) {
        this.token.next(token);
    }

}
