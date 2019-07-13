
/* booleans */
let b0:boolean;
let b1:boolean = true;
let b2:boolean = false;

/* number */
let n0: number;
let n1: number = 6;
let n2: number = 0xf00d;
let n3: number = 0b1010;
let n4: number = 0o744;
let n5: number = 1.3;

let n6: number = -6;
let n7: number = -0xf00d;
let n8: number = -0b1010;
let n9: number = -0o744;
let n10: number = -1.3;

let n11: number = 123e5;
let n12: number = 123e-5;

/* strings */
let str1: string = 'hello';
let str2: string = "hello";
const str3: string = `hello`;

/* undefined */
let u0:undefined;

/* null */
let nl:null = null;

/* objects */
let o0:object;
let o1:object = {};
let o2:object = {
    name:3,
    name2:"pete"
};

/* Function variable declarations */
let f0:()=>void;
let f1:(a:number)=>number;
let f2:()=>void             = function(){};

/* Implicit function variables */
let f3 = ()=>{};
let f4 = (a:number):void => {};
let v5 = (a:number)=> 3;



