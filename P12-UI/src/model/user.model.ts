export class User {

  constructor(username, password, confirm){
    this.userName = username;
    this.passWord = password;
    this.passWordConfirm = confirm;
  }

  id: number;
  userName: string;
  passWord: string;
  passWordConfirm: string;
}
