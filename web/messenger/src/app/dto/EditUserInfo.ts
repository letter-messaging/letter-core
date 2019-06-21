import {User} from './User';
import {MaritalStatus} from './enum/MaritalStatus';
import {Avatar} from './Avatar';

export class EditUserInfo {
	user: User;
	firstName: string;
	lastName: string;
	gender: boolean;
	birthDate: string;
	maritalStatus: MaritalStatus;
	country: string;
	city: string;
	location: string;
	phoneNumber: string;
	mail: string;
	placeOfEducation: string;
	placeOfWork: string;
	about: string;
	avatars: Avatar[];
	newAvatar: File;
}
