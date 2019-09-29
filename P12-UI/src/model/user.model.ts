import {Mail} from './mail.model';

export class User {

  constructor() { }

  id: number;
  userName: string;
  passWord: string;
  passWordConfirm: string;
  oldPassWord: string;
  mail: Mail;
  email: string;
}
