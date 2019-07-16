

type A = {
    one:boolean,
    two:string,
    three:number
};

/* "one" | "two" | "three" */
type B = keyof A;

type C = keyof {
    one:boolean,
    two:string,
    three:number
};

/* "toString" | "toFixed" | "toExponential" | "toPrecision" | "valueOf" | "toLocaleString" */
type D = keyof number;

let a = 10;
type E = keyof typeof a;
