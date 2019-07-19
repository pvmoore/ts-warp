
/* basic types */
type a = boolean;
type b = number;
type c = string;
type d = null;
type e = undefined;

/* objects */
type f = object;
type g = {a:number};
type h = Object;

/* arrays */
type a1 = number[];

/* tuples */
type t1 = [number];
type t2 = [string, boolean];

/* typeof */
let obj = 100;
type i = typeof obj;

/* unions */
type u1 = number | null;
type u2 = "hello" | "there" | 30 | true;

/* intersections */
type i1 = object & {a:number};