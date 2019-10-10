export class SearchCriteria {

  constructor(key: string, operation: string, value: any) {
    this.key = key;
    this.operation = operation;
    this.value = value;
  }

  public key: string;
  public operation: string;
  public value: any;
}
