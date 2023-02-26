"use strict";

const VAR_INDEXES = {
    "x" : 0,
    "y" : 1,
    "z" : 2
};

const cnst = (a) => (...vars) => a;
const pi = cnst(Math.PI);
const e = cnst(Math.E);
const variable = (a) => (...vars) => vars[VAR_INDEXES[a]];

const multiOperation = function(f) {
    let foo = (...args) => function(...vars) {
        // :NOTE: map
        let cnt = [];
        for (const v of args) {
            cnt.push(v(...vars))
        }
        return f(...cnt)
    };
    foo.len = f.length;
    return foo;
};

const add = multiOperation((x, y) => x + y);
const subtract = multiOperation((x, y) => x - y);
const multiply = multiOperation((x, y) => x * y);
const divide = multiOperation((x, y) => x / y);
const negate = multiOperation(a => -a);

const averageOf3 = (a, b, c) => (a + b + c) / 3;

const avg3 = multiOperation(averageOf3);
const medOf5 = function(a, b, c, d, e) {
    // :NOTE: CP
    let arr = [a, b, c, d, e];
    return arr.sort((a, b) => a - b)[2]
};
const med5 = multiOperation(medOf5);

const get = function(stack, k) {
    // :NOTE: .splice
    let arr = [];
    for (let i = 0; i < k; i++) {
        arr.unshift(stack.pop())
    }
    return arr
};

const OPERATIONS = {
    "+" : add,
    "-" : subtract,
    "/" : divide,
    "*" : multiply,
    "avg3" : avg3,
    "med5" : med5,
    "negate" : negate
};

const CONSTANTS = {
    "pi" : pi,
    "e" : e
};

// :NOTE: Эффективность
const parse = (a) => (x, y, z) => {
    let arr = a.trim().split(/\s+/g);
    let stack = [];
    for (const v of arr) {
        if (v in CONSTANTS) {
            stack.push(CONSTANTS[v]);
        } else if (v in OPERATIONS) {
            stack.push(OPERATIONS[v](...get(stack, OPERATIONS[v].len)))
        } else if (v in VAR_INDEXES) {
            stack.push(variable(v))
        } else {
            stack.push(cnst(parseInt(v)))
        }
    }
    return stack[0](x, y, z)
};
