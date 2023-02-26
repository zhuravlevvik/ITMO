(defn constant [x] (constantly x))
(defn variable [x] (fn [vars] (vars (name x))))
(defn negate [x] (fn [vars] (- (x vars))))
(defn operation [f, & args] (fn [vars] (apply f (mapv (fn [a] (a vars)) args)))) ;♥
(def add (partial operation +))
(def subtract (partial operation -))
(def multiply (partial operation *))
(defn div ([a] (/ 1.0 (double a))) 
		  ([a & b] (reduce (fn [x y] (/ (double x) (double y))) a b)))
(defn divide [& args] (apply (partial operation div) args))
(defn sumexp [& args] (fn [vars] (apply + (mapv (fn [x] (Math/pow Math/E (x vars))) args))))
(def softmax (fn [& args] (divide (sumexp (first args)) (apply sumexp args))))
(def operations {'+ add '- subtract '* multiply '/ divide 'negate negate 'sumexp sumexp 'softmax softmax})
 
(defn parseExpression [el] 
	(if (number? el) (constant el) 
		(if (symbol? el) (variable el)
			(apply (operations (first el)) (mapv parseExpression (rest el))))))
(defn parseFunction [str] (parseExpression (read-string str)))

(definterface Operation 
	(evaluate [vars])
	(str [])
	(diff [x]))

(deftype AbstractElement [evl toStr df a]
	Operation
	(evaluate [this vars] (evl vars))
	(str [this] toStr)
	(diff [this x] (df x)))

(defn Constant [a] (AbstractElement. (constantly a) (str a) (fn [x] (Constant 0)) a))
(defn Variable [v] (AbstractElement. (fn [vars] (vars (name v))) (str v) (fn [x] (if (= (name v) x) (Constant 1) (Constant 0))) v))

(deftype AbstractOperation [f df op args]
	Operation
	(evaluate [this vars] (apply f (map (fn [x] (.evaluate x vars)) args)))
	(str [this] (str "(" op " " (clojure.string/join " " (map (fn [x] (.str x)) args)) ")"))
	(diff [this x] (df x)))

(defn Add [& args] (AbstractOperation. + 
	(fn [x] (apply Add (map (fn [v] (.diff v x)) args)))
	"+" args))
(defn Subtract [& args] (AbstractOperation. - 
	(fn [x] (apply Subtract (map (fn [v] (.diff v x)) args)))
	"-" args))
(defn Negate [a] (AbstractOperation. (fn [a] (- a)) (fn [x] (Negate (.diff a x))) "negate" (list a)))
(defn Multiply [& args]
	(defn mulDiff [x & a] (if (= 1 (count a)) (.diff (first a) x)  
							(Add (Multiply (.diff (first a) x) (apply Multiply (rest a))) 
								 (Multiply (first a) (apply (partial mulDiff x) (rest a))))))
	(AbstractOperation. * (fn [x] (apply (partial mulDiff x) args)) "*" args))
(defn Divide [& args] 
	(defn divDiffForOne [x a] (.diff (Divide (Constant 1) a) x))
	(defn divDiff [x & a] 
		(if (= 1 (count a)) (.diff (first a) x)
			(Divide (Subtract (Multiply (.diff (first a) x) (apply Multiply (rest a))) 
							  (Multiply (first a) (apply (partial divDiff x) (rest a)))) 
					(apply Multiply (concat (rest a) (rest a))))))
	(AbstractOperation. div (fn [x] (if (= 1 (count args)) (divDiffForOne x (first args)) (divDiff x (first args) (apply Multiply (rest args))))) "/" args))

(defn sumOfExp [& args] (apply + (map (fn [x] (Math/pow Math/E x)) args)))

(defn Sumexp [& args] 
	(defn expDiff [x a] (Multiply (Sumexp a) (.diff a x)))
	(defn sumexpDiff [x & a] (apply Add (map (partial expDiff x) a)))
	(AbstractOperation. sumOfExp 
						(fn [x] (apply (partial sumexpDiff x) args))
						"sumexp" args))

(defn Softmax [& args]
	(def expr (Divide (Sumexp (first args)) (apply Sumexp args)))
	(AbstractOperation. (fn [& lst] (/ (double (sumOfExp (first lst))) (double (apply sumOfExp lst))))
						(fn [x] (.diff expr x))
						"softmax" args))

(def objOperations {'+ Add '- Subtract '* Multiply '/ Divide 'negate Negate 'sumexp Sumexp 'softmax Softmax})

(defn parseElement [el]
	(if (number? el) (Constant el)
		(if (symbol? el) (Variable el)
			(apply (objOperations (first el)) (mapv parseElement (rest el))))))
(defn parseObject [str] (parseElement (read-string str)))

(defn evaluate [expr vars] (.evaluate expr vars)) 
(defn toString [expr] (.str expr))
(defn diff [expr x] (.diff expr x))

;(println (evaluate (diff (Sumexp (Variable "x") (Variable "y") (Variable "z")) "x")  {"x" 1 "y" 1 "z" 1}))
;(println (toString (diff (Sumexp (Variable "x") (Variable "y") (Variable "z")) "x")))