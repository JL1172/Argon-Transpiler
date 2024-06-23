const { Schema } = require("../src.v3/index");
// import { Schema } from "../src.v3/index";

const schema = new Schema();
schema.build({
  first_name: { string: true, required: true, min: 3 },
  last_name: { string: true, min: 3 },
});
console.log(schema.read_schema());
const input = {last_name: "jsldkf", first_name: true};
schema.validate(input);
