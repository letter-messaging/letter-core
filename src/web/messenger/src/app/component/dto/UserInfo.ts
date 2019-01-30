import {User} from './User';
import {MaritalStatus} from './enum/MaritalStatus';
import {Avatar} from './Avatar';

export class UserInfo {

	user: User;
	firstName: string;
	lastName: string;
	gender: boolean;
	birthDate: Date;
	maritalStatus: MaritalStatus;
	country: string;
	city: string;
	location: string;
	phoneNumber: string;
	mail: string;
	placeOfEducation: string;
	placeOfWork: string;
	about: string;
	avatars: Array<Avatar>;

}
