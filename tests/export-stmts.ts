
/* star */
export * from "mod";

/* default Statement */
export default A;
export default class A{}
//export default {obj:"name"}   // fixme
export default "hello";

/* declare Declaration */
export declare class Cde {}

/* Declaration */
export class Bcd {}

/* List */
export { B as Boo, A, C as default };
export { A } from "mod";
