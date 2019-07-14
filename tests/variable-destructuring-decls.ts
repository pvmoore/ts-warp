
//const array:number[] = [1,2,3];
//const tuple:[number,string,number] = [1,'2',3];
//const obj:{a:number, b:string, c:number} = {a:1, b:'2', c:3};

{
    /* Destructured arrays */
    const [a,b,c] = array; // a=1, b=2, c=3
    let [a2,b2,c2]:number[] = array;
    let [a3,b3] = array; // a3=1, b3=2
    let [a4] = array;    // a4 = 1
    let [,b4] = array;   // b4 = 2
    let [,,c4] = array;  // c4 = 3
    let [a5,b5,c5,d5] = array;  // a5=1, b5=2, c5=3, d5=undefined
    let [a6, ...rest1] = array; // a6=1, rest1 = [2,3]
    let [...rest2] = array; // rest2 = [1,2,3]
}
{
    /* Destructured tuples */
    const [a,b,c] = tuple;
    let [t12,t22,t32]:[number,string,number] = tuple;
    let [t1] = tuple;   // t1=1
    let [,t2] = tuple;  // t2='2'
    let [,,t3] = tuple; // t3=3
    //let [ta1, tb1,tc1, td1] = tuple;  // error too many names
    let [t4, ...rest3] = tuple; // t4=1, rest3 = ['2',3]:[string,number]
    let [...rest4] = tuple; // rest4 = [1,'2',3]:[number,string,number]
}
{
    /* Destructured objects */
    const {a,b,c} = obj;
    let {a:renamedA} = obj;  // renamedA=1
    //let {d} = obj;  // error no property 'd'
    let {} = obj;   // this works but what does it do?
    let {a:newA}:{a:number} = obj;
    let {b:newB, ...rest5} = obj; // rest5 = {a:1,c:3}
    let {a:oa,b:ob,c:oc,...rest6} = obj;  // rest6 = {}
}
