
# Notes

## Using interfaces with new() methods

```
interface OC {
    new(value: number): any;
}
/* create using class expression */
const Clock: OC = class Clock  {
  constructor(h: number) {}
}

/* create using static type */
class MyOC {
    constructor(value: number) {}
}
function create(oc:OC) {
  return new oc(3);
}
let a:OC = create(MyOC);
```

## Interface function types

```
interface I {
    (value: number): any;
}
let a:I = function(n:number) {}
let b:I = (n:number)=> {}
```

## Hybrid interfaces

```
interface I {
    (n:number):any
    prop:number
    method():void
}
const a:I = function(n:number) {} as I; // cast required here
a.prop = 2;
a.method = ()=>{};
```

## Function overloading

- Overloaded functions must have a different number of parameters eg.
```
function foo(n:number) {}
function foo(n:number, p:number) {}
```
- Alternatively, you can overload with the same number of parameters if you use this workaround:
```
function foo(name: string): string;
function foo(cust: number): string;

function foo(p: string | number):string {
  if(typeof p === "string") {} else {}
  return ""
}
```