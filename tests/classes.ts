
class C {
    prop:number;
    prop2:number;
    private prop4:number = 4;

    constructor(prop:number, prop2:number, private prop3:number=3) {
        //this.prop = prop;
        //this.prop2 = prop2;
    }
    method(a:number, b?:number, c:number=0) {
        return a;
    }
}
//let c:C = new C(1,2);