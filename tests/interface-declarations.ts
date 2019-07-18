
interface A {}
interface B {
    prop:number
}
interface C {
    prop
}
interface D {
    p?:string,
    readonly p2:number
    method(n:string):void
}

/* Indexable types */
interface S {
    [prop:string] : string;   
    prop2:string;    
}

interface PropertyDescriptorMap {
    [s: string]: PropertyDescriptor;
}

/* new */
interface ObjectConstructor {
    new(value?: any): Object;
    (): any;
 //   (value: any): any;
}