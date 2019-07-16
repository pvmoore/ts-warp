
/* As an expression */
{
    const a = typeof "";
        
    function foo() {}
    const b = typeof foo;
}
/* As a type */
{
    const a = "";
    let b:typeof a;
    let c:typeof a = ".";
}
