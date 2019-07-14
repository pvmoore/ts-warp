
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
let o3:{a:number};
let o4:{};  // empty object
let o5:{p}; // p:any
let o6:{p:string p2 p3:number};
let o7:{p,p2;p3};

/* any */
let a0:any;
let a1:any = 1;

/* Function variable declarations */
let f0:()=>void;
let f1:(a:number)=>number;
let f2:()=>void             = function(){};

/* Implicit function variables */
let f3 = ()=>{};
let f4 = (a:number):void => {};
let v5 = (a:number)=> 3;

/* arrays */
let ar0:number[];
let ar1:string[];
let ar2:boolean[];
let ar3:object[];

/* tuples */
let t0:[number];
let t1:[number,boolean];
let t2:[number,boolean,string];
let t3:[()=>void, (a:string)=>number];

/* bigint */
let bi0:bigint;
//let bi1:bigint = BigInt(10);  
//let bi2:bigint = 100n;
