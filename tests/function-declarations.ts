

/* Function declarations */
function foo() {}
function foo2(n:number) {}
function foo3():void {}
function foo4(a) {}         // a:any

function bar(a:number = 3) {}

/* rest parameters */

function baz(a:number, ...rest:string[]):void {}

/* destructured parameters */
function boo(n:number, [a,b,...rest]:number[] ) {}
function baa( {a,b}:{a:number,b:string} ) {}
