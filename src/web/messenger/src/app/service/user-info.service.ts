import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {API_URL} from '../../../globals';
import {Observable} from 'rxjs';
import {UserInfo} from '../dto/UserInfo';

@Injectable({
	providedIn: 'root'
})
export class UserInfoService {

	constructor(private http: HttpClient) {
	}

	get(token: string, userId: number): Observable<UserInfo> {
		return this.http.get<UserInfo>(API_URL + 'info', {
			params: {
				userId: userId.toString()
			},
			headers: {
				'Auth-Token': token
			}
		});
	}

	edit(token: string, userInfo: UserInfo): Observable<UserInfo> {
		return this.http.post<UserInfo>(API_URL + 'info', userInfo, {
			headers: {
				'Auth-Token': token
			}
		});
	}

}
