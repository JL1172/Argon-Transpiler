const {Schema} = require("../src.v3/index");
// import { Schema } from "../src.v3/index";

const schema = new Schema();
schema.build({first_name: {string: true, required: true, min: 0, max: 1}});
console.log(schema.read_schema());
