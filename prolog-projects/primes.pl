prime(2).
prime(3).
prime(5).
prime(7).
prime(11).
prime(13).
prime(17).
prime(19).
prime(R) :- prime_table(R, 1) ; \+ composite(R).

composite(1).
composite(R) :- 0 is mod(R, 2), R =\= 2, !.
composite(R) :- 0 is mod(R, 3), R =\= 3, !.
composite(R) :- 0 is mod(R, 5), R =\= 5, !.
composite(R) :- 0 is mod(R, 7), R =\= 7, !.
composite(R) :- 0 is mod(R, 11), R =\= 11, !.
composite(R) :- 0 is mod(R, 13), R =\= 13, !.
composite(R) :- 0 is mod(R, 17), R =\= 17, !.
composite(R) :- 0 is mod(R, 19), R =\= 19, !.
composite(R) :- prime_table(R, C), C =\= 1.
init(N) :-  sieve(N, 23).

sieve(N, C) :- S is C * C, S =< N, composite(C), C1 is C + 2, sieve(N, C1).
sieve(N, C) :- S is C * C, S =< N, assert(prime_table(C, 1)), C1 is C * C, fill(N, C1, C), C2 is C + 2, sieve(N, C2).
sieve(N, C) :- S is C * C, S > N.

fill(N, C, S) :- C > N, \+ composite(C), assert(prime_table(C, S)).
fill(N, C, S) :- \+ composite(C), assert(prime_table(C, S)), C1 is C + S, fill(N, C1, S).
fill(N, C, S) :- C > N, composite(C).
fill(N, C, S) :- composite(C), C1 is C + S, fill(N, C1, S).

prime_divisors(1, []) :- !.
prime_divisors(N, [N]) :- prime(N), !.
prime_divisors(N, [H | T]) :- number(N), take_div(N, H), N1 is N / H, prime_divisors(N1, T).
prime_divisors(N, L) :- \+ number(N), check_array(L), foldLeft(L, 1, mul, N), !.

check_array([]).
check_array([H]) :- prime(H).
check_array([H1, H2 | T]) :- prime(H1), prime(H2), H1 =< H2, check_array([H2 | T]).

foldLeft([], Z, _, Z).
foldLeft([H | T], Z, F, R) :- G =.. [F, Z, H, RH], call(G), foldLeft(T, RH, F, R).

mul(A, B, R) :- R is A * B.

take_div(N, 2) :- 0 is mod(N, 2), !.
take_div(N, 3) :- 0 is mod(N, 3), !.
take_div(N, 5) :- 0 is mod(N, 5), !.
take_div(N, 7) :- 0 is mod(N, 7), !.
take_div(N, 11) :- 0 is mod(N, 11), !.
take_div(N, 13) :- 0 is mod(N, 13), !.
take_div(N, 17) :- 0 is mod(N, 17), !.
take_div(N, 19) :- 0 is mod(N, 19), !.
take_div(N, H) :- prime_table(N, H).

lcm(A, B, LCM) :- prime_divisors(A, LA), prime_divisors(B, LB), cnt_lcm(LA, LB, 1, mul, LCM).

cnt_lcm([], [], Z, _, Z).
cnt_lcm([H1 | T], [], Z, F, R) :- G =.. [F, Z, H1, RH], call(G), cnt_lcm(T, [], RH, F, R).
cnt_lcm([], [H2 | T], Z, F, R) :- G =.. [F, Z, H2, RH], call(G), cnt_lcm([], T, RH, F, R).
cnt_lcm([H1 | T1], [H2 | T2], Z, F, R) :- H1 = H2, G =.. [F, Z, H1, RH], call(G), cnt_lcm(T1, T2, RH, F, R).
cnt_lcm([H1 | T1], [H2 | T2], Z, F, R) :- H1 < H2, G =.. [F, Z, H1, RH], call(G), cnt_lcm(T1, [H2 | T2], RH, F, R).
cnt_lcm([H1 | T1], [H2 | T2], Z, F, R) :- H2 < H1, G =.. [F, Z, H2, RH], call(G), cnt_lcm([H1 | T1], T2, RH, F, R).