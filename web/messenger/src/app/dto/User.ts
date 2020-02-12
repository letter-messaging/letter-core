import {Avatar} from './Avatar';

export class User {
    id: number;
    login: string;
    firstName: string;
    lastName: string;
    lastSeen: Date;
    avatar: Avatar;
}
