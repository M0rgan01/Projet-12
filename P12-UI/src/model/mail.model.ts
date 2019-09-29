export class Mail {

  public id: number;
  public email: string;
  public tryToken: number;
  public expiryToken: Date;
  public availablePasswordRecovery: boolean;
  public expiryPasswordRecovery: Date;
}
