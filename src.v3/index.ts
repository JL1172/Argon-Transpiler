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
  private report_error(err: string) {
    console.error(err);
    process.exit(1);
  }
  public build(schema: Record<string, options>): void {
    const allowed_keys = {
      required: "boolean",
      string: "boolean",
      boolean: "boolean",
      number: "boolean",
      number_string: "boolean",
      min: "number",
      max: "number",
      password: "boolean",
      email: "boolean",
    };
    //validates no keys outside of options type allowed keys exist
    for (const field_key in schema) {
      const first_key = field_key;
      const rules = schema[first_key];
      for (const key in rules) {
        if (!(key in allowed_keys)) {
          this.report_error(
            `Schema Build Error: Property ${key} is illegal option for schema build.`
          );
        }
      }
    }
    //validates no keys in options are assigned the wrong type.
    for (const field_key in schema) {
      const rules = schema[field_key];
      for (const key in rules) {
        if (typeof rules[key] !== allowed_keys[key]) {
          this.report_error(
            `Schema Build Error: Property ${key} expected type: ${
              allowed_keys[key]
            } and recieved type: ${typeof rules[key]}`
          );
        }
      }
    }
    //validates illogical schema's are not created (multiple types, higher min than max);
    for (const field_key in schema) {
      let isTypeAssigned: number = 0;
      const rules = schema[field_key];
      for (const key in rules) {
        if (key !== "required" && key !== "min" && key !== "max") {
          if (rules[key] === true && isTypeAssigned === 0) {
            isTypeAssigned++;
          } else if (rules[key] === true && isTypeAssigned > 0) {
            this.report_error(
              `Schema Build Error: Cannot assign multiple types. Re-read code and validate only one type per field is assigned.`
            );
          }
        } else {
          if ("min" in rules && "max" in rules) {
            const minV = rules["min"];
            const maxV = rules["max"];
            if (minV > maxV) {
              this.report_error(
                `Schema Build Error: Illogical min and max constraints applied.`
              );
            }
          } else if ("min" in rules) {
            if (rules["min"] < 0) {
              this.report_error(
                `Schema Build Error: Illogical min constraint applied.`
              );
            }
          } else if ("max" in rules) {
            if (rules["max"] === Infinity) {
              this.report_error(
                `Schema Build Error: Illogical max constraint applied.`
              );
            }
          }
        }
      }
    }
    //setting defaults if required doesnt exist, its set as false
    for (const key in schema) {
      const curr_key = schema[key];
      if (!("required" in curr_key)) {
        curr_key["required"] = false;
      }
    }
    this.schema = schema;
  }
  public read_schema(): Record<string, options> {
    return this.schema;
  }
  // gotta figure out return value
  public validate(inputs: Record<string, string | boolean | number>) {
    for (const key in inputs) {
      if (!(key in this.schema)) {
        this.report_error(
          `Schema Validation Error: Unexpected key ${key} in input.`
        );
      }
    }
    //this scans schema to see what to expect that is required
    const required_keys = {};
    for (const key in this.schema) {
      const rules = this.schema[key];
      if ("required" in rules === true) {
        required_keys[key] = "";
      }
    }
    //validates whether or not all the required keys are present
    for (const key in required_keys) {
      if (!(key in inputs)) {
        this.report_error(
          `Schema Validation Error: Expected Key: ${key} in input.`
        );
      }
    }

    //type checking
    for (const key in this.schema) {
      const expected_type: [string, boolean | number] = Object.entries(
        this.schema[key]
      ).filter(
        (n) =>
          n[0] !== "required" &&
          n[0] !== "min" &&
          n[0] !== "max" &&
          n[1] === true
      )[0];
      const value = inputs[key];
      if (typeof value !== expected_type[0]) {
        this.report_error(
          `Schema Validation Error. Improper type for key ${key}, expected: ${expected_type[0]}`
        );
      }
    }
    //todo compare input keys and expected keys
  }
}
