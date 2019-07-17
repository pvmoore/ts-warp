
let a = 10;

// no-ops
;;

switch(a) {
    case 1:
        break;
    default:
        break;
    case 2:
    case 3:
        break;
    case 4: {
        break;
    }
}
