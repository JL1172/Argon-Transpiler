export type options = {
  required?: boolean;
  string?: boolean;
  boolean?: boolean;
  number?: boolean;
  number_string?: boolean;
  min?: number;
  max?: number;
  password?: boolean;
  email?: boolean;
};
class Email {
  protected static readonly email_regex: RegExp =
    /^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])$/i;
}
export class Schema {
  private schema: Record<string, options>;
  public build(schema: Record<string, options>): void {
    this.schema = schema;
  }
  public read_schema(): Record<string, options> {
    return this.schema;
  }
  // gotta figure out return value
  public validate(inputs: Record<string,string | boolean | number>) {
    const keys = Object.keys(inputs);
    
  }
}
