"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.Schema = void 0;
var Email = /** @class */ (function () {
    function Email() {
    }
    Email.email_regex = /^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])$/i;
    return Email;
}());
var Schema = /** @class */ (function () {
    function Schema() {
    }
    Schema.prototype.report_error = function (err) {
        console.error(err);
        process.exit(1);
    };
    Schema.prototype.build = function (schema) {
        var allowed_keys = {
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
        for (var field_key in schema) {
            var first_key = field_key;
            var rules = schema[first_key];
            for (var key in rules) {
                if (!(key in allowed_keys)) {
                    this.report_error("Schema Build Error: Property ".concat(key, " is illegal option for schema build."));
                }
            }
        }
        //validates no keys in options are assigned the wrong type.
        for (var field_key in schema) {
            var rules = schema[field_key];
            for (var key in rules) {
                if (typeof rules[key] !== allowed_keys[key]) {
                    this.report_error("Schema Build Error: Property ".concat(key, " expected type: ").concat(allowed_keys[key], " and recieved type: ").concat(typeof rules[key]));
                }
            }
        }
        //validates illogical schema's are not created (multiple types, higher min than max);
        for (var field_key in schema) {
            var isTypeAssigned = 0;
            var rules = schema[field_key];
            for (var key in rules) {
                if (key !== "required" && key !== "min" && key !== "max") {
                    if (rules[key] === true && isTypeAssigned === 0) {
                        isTypeAssigned++;
                    }
                    else if (rules[key] === true && isTypeAssigned > 0) {
                        this.report_error("Schema Build Error: Cannot assign multiple types. Re-read code and validate only one type per field is assigned.");
                    }
                }
                else {
                    if ("min" in rules && "max" in rules) {
                        var minV = rules["min"];
                        var maxV = rules["max"];
                        if (minV > maxV) {
                            this.report_error("Schema Build Error: Illogical min and max constraints applied.");
                        }
                    }
                    else if ("min" in rules) {
                        if (rules["min"] < 0) {
                            this.report_error("Schema Build Error: Illogical min constraint applied.");
                        }
                    }
                    else if ("max" in rules) {
                        if (rules["max"] === Infinity) {
                            this.report_error("Schema Build Error: Illogical max constraint applied.");
                        }
                    }
                }
            }
        }
        //setting defaults if required doesnt exist, its set as false
        for (var key in schema) {
            var curr_key = schema[key];
            if (!("required" in curr_key)) {
                curr_key["required"] = false;
            }
        }
        this.schema = schema;
    };
    Schema.prototype.read_schema = function () {
        return this.schema;
    };
    // gotta figure out return value
    Schema.prototype.validate = function (inputs) {
        for (var key in inputs) {
            if (!(key in this.schema)) {
                this.report_error("Schema Validation Error: Unexpected key ".concat(key, " in input."));
            }
        }
        //this scans schema to see what to expect that is required
        var required_keys = {};
        for (var key in this.schema) {
            var rules = this.schema[key];
            if ("required" in rules === true) {
                required_keys[key] = "";
            }
        }
        //validates whether or not all the required keys are present
        for (var key in required_keys) {
            if (!(key in inputs)) {
                this.report_error("Schema Validation Error: Expected Key: ".concat(key, " in input."));
            }
        }
        //type checking
        for (var key in this.schema) {
            var expected_type = Object.entries(this.schema[key]).filter(function (n) {
                return n[0] !== "required" &&
                    n[0] !== "min" &&
                    n[0] !== "max" &&
                    n[1] === true;
            })[0];
            var value = inputs[key];
            if (typeof value !== expected_type[0]) {
                this.report_error("Schema Validation Error. Improper type for key ".concat(key, ", expected: ").concat(expected_type[0]));
            }
        }
        //todo compare input keys and expected keys
    };
    return Schema;
}());
exports.Schema = Schema;
