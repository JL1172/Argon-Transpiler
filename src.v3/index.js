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
            number: "bool,ean",
            number_string: "boolean",
            min: "number",
            max: "number",
            password: "boolean",
            email: "boolean",
        };
        for (var field_key in schema) {
            var first_key = field_key;
            var rules = schema[first_key];
            for (var key in rules) {
                if (!(key in allowed_keys)) {
                    this.report_error("Schema Build Error: Property: ".concat(key, " is illegal option for schema build."));
                }
            }
        }
        for (var field_key in schema) {
            var rules = schema[field_key];
            for (var key in rules) {
                if (typeof rules[key] !== allowed_keys[key]) {
                    this.report_error("Schema Build Error: Property: ".concat(key, " expected type ").concat(allowed_keys[key], " and recieved ").concat(typeof rules[key]));
                }
            }
        }
        this.schema = schema;
    };
    Schema.prototype.read_schema = function () {
        return this.schema;
    };
    // gotta figure out return value
    Schema.prototype.validate = function (inputs) {
        var input_keys = Object.keys(inputs);
        var expected_keys = [];
        for (var key in this.schema) {
            if (this.schema[key].required === true) {
                expected_keys.push(key);
            }
        }
        //todo compare input keys and expected keys
    };
    return Schema;
}());
exports.Schema = Schema;
