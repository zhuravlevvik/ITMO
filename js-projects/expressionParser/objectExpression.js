"use strict"

const VAR_INDEXES = {
    "x" : 0,
    "y" : 1,
    "z" : 2
}

function Element(toStr, evl, df, pref, post) {
    return {
        evaluate: function(...vars) { return evl(...vars) },
        toString: function() { return toStr() },
        diff: function(d) { return df(d) },
        prefix: function() { return pref() },
        postfix: function() { return post() }
    }
}

function Const(a) {
    return Element(() => '' + a, (...vars) => a, (d) => new Const(0), () => '' + a, () => '' + a)
}

function Variable(x) {
    return Element(
            () => x,
            (...vars) => vars[VAR_INDEXES[x]],
            (a) => a === x ? new Const(1) : new Const(0),
            () => x,
            () => x
    )
}

function MultiOperation(operation, f, df) {
    return function(...args) {
        return Element(
                ()  => [...args].map(v => v.toString()).join(" ") + " " + operation,
                function(...vars) {
                    let arr = []
                    for (const v of args) {
                        arr.push(v.evaluate(...vars))
                    }
                    return f(...arr)
                },
                df,
                () => "(" + operation + " " + [...args].map(v => v.prefix()).join(" ") + ")",
                () => "(" + [...args].map(v => v.postfix()).join(" ") + " " + operation + ")"
            )
    }
}

function Add(a, b) {
    return MultiOperation("+", (a, b) => a + b, (x) => new Add(a.diff(x), b.diff(x)))(a, b)
}

function Subtract(a, b) {
    return MultiOperation("-", (a, b) => a - b, (x) => new Subtract(a.diff(x), b.diff(x)))(a, b)
}

function Multiply(a, b) {
    return MultiOperation("*", (a, b) => a * b, (x) => new Add(new Multiply(a.diff(x), b), new Multiply(a, b.diff(x))))(a, b)
}

function Divide(a, b) {
    let dif = function(x) { return new Divide(new Subtract(new Multiply(a.diff(x), b), new Multiply(a, b.diff(x))), new Multiply(b, b)) }
    return MultiOperation("/", (a, b) => a / b, dif)(a, b)
}

function Negate(a) {
    return MultiOperation("negate", (a) => -a, (x) => new Negate(a.diff(x)))(a)
}

function Gauss(a, b, c, x) {
    let dif = function(d) {
        let power = new Negate(new Divide(new Multiply(new Subtract(x, b), new Subtract(x, b)), new Multiply(new Const(2), new Multiply(c, c))))
        return new Add(new Multiply(a.diff(d), new Divide(new Gauss(a, b, c, x), a)), new Multiply(new Gauss(a, b, c, x), power.diff(d)))
    }
    return MultiOperation("gauss", (a, b, c, x) => a * Math.exp(-((x - b) * (x - b)) / (2 * c * c)), dif)(a, b, c, x)
}

function Sumexp(...args) {
    let dif = function(d) {
        let expr = new Multiply(new Sumexp(args[0]), args[0].diff(d))
        for (let i = 1; i < args.length; i++) { expr = new Add(new Multiply(new Sumexp(args[i]), args[i].diff(d)), expr) }
        return expr
    }
    return MultiOperation("sumexp", (...a) => [...a].map(Math.exp).reduce((a, b) => a + b, 0), dif)(...args)
}

function Softmax(...args) {
    const sumOfExp = (...a) => [...a].map(Math.exp).reduce((a, b) => a + b, 0)
    let expr = new Divide(new Sumexp(args[0]), new Sumexp(...args))
    return MultiOperation("softmax", (...a) => sumOfExp(a[0]) / sumOfExp(...a) , (d) => expr.diff(d))(...args)
}

const get = function(stack, k) {
    let arr = []
    for (let i = 0; i < k; i++) {
        arr.unshift(stack.pop())
    }
    return arr
}

const OPERATIONS = {
    "+" : Add,
    "-" : Subtract,
    "/" : Divide,
    "*" : Multiply,
    "negate" : Negate,
    "gauss": Gauss,
    "sumexp": Sumexp,
    "softmax": Softmax
}

function ParseException(message) {
    this.message = message
}

const parse = function(a) {
    let arr = a.trim().split(/\s+/g)
    let stack = []
    for (const v of arr) {
        if (v in OPERATIONS) {
            stack.push(new OPERATIONS[v](...get(stack, OPERATIONS[v].length)))
        } else if (v in VAR_INDEXES) {
            stack.push(new Variable(v))
        } else {
            stack.push(new Const(parseInt(v)))
        }
    }
    return stack[0]
}

const parseToArray = function(string) {
    let arr = string.trim().split(/\s+/g);
    let res = []
    for (const v of arr) {
        let s = v.replace(/[(]/g, " ( ").replace(/[)]/g, " ) ").trim().split(/\s+/g);
        res = res.concat(s)
    }
    return res
}

let pos = 0

const parseElement = function(arr, ppf) {
    if (arr[pos] === '(') {
        return ppf ? parseExpression(arr, ppf) : parseExpression(arr, ppf)
    }
    if (arr[pos] in OPERATIONS) {
        throw new ParseException("Only Const or Variable expected as there is only one element but found: " + arr[pos])
    }
    if (arr[pos] in VAR_INDEXES) {
        return new Variable(arr[pos++])
    }
    let res = parseInt(arr[pos])
    if (res.toString() === arr[pos]) {
        pos++
        return new Const(res)
    }
    throw new ParseException("Argument of operation expected but found: " + arr[pos])
}

const parseOperation = function(arr) {
    if (!(arr[pos] in OPERATIONS)) {
        throw new ParseException("Operation expected but found: " + arr[pos])
    }
    return arr[pos++]
}

const parseArguments = function(arr, ppf) {
    let args = []
    while (true) {
        if (ppf && arr[pos] === ")" || !ppf && arr[pos] in OPERATIONS) {
            break;
        }
        args.push(parseElement(arr, ppf))
        if (pos === arr.length) {
            throw new ParseException("Argument(s) of operation expected but found eof")
        }
    }
    return args
}

const parseExpression = function(arr, ppf) {
    if (pos === arr.length) {
        return;
    }
    if (arr[pos] === '(') {
        pos++
        let operation
        let args = []
        if (ppf) {
            operation = parseOperation(arr)
            args = parseArguments(arr, ppf)
        } else {
            args = parseArguments(arr, ppf)
            operation = parseOperation(arr)
        }
        if (arr[pos++] !== ')') {
            throw new ParseException(") expected after " + args)
        }
        if (OPERATIONS[operation].length !== 0 && OPERATIONS[operation].length !== args.length) {
            throw new ParseException("Expected " + OPERATIONS[operation].length + " arguments for operation " + operation + " but found " + args.length)
        }
        return new OPERATIONS[operation](...args)
    } else {
         if (arr.length > 1) {
             throw new ParseException("Only one element expected as there is no brackets")
         }
         return parseElement(arr, ppf)
     }
}

const parsePrefix = (string) => parsePPE(string, true)
const parsePostfix = (string) => parsePPE(string, false)

const parsePPE = function(string, ppf) {
    pos = 0
    let arr = parseToArray(string)
    let expr = parseExpression(arr, ppf);
    if (pos < arr.length) {
        throw new ParseException("End of expression expected but found: " + arr.slice(pos))
    }
    return expr
}
