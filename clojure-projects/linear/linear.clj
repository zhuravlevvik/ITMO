(defn scal [x] x)
(defn vct [vector] vector)
(defn matrix [mat] mat)
(defn of_numbers [v] (every? (fn [a] (number? a)) v))
(defn of_vectors [v] (every? (fn [a] (vector? a)) v))
(defn is_vector [v] (and (vector? v) (of_numbers v)))
(defn is_matrix [m] (every? (fn [v] (is_vector v)) m))
(defn vect_func [f, of, & vectors] 
	{:pre [(and (every? (fn [v] (vector? v)) vectors)
				(every? (fn [v] (== (count (first vectors)) (count v))) vectors)
				(every? (fn [v] (of v)) vectors))]
	 :post [(and (of %) (== (count %) (count (first vectors))))]}
	(apply mapv f vectors))
(def v+ (partial vect_func + of_numbers))
(def v- (partial vect_func - of_numbers))
(def v* (partial vect_func * of_numbers))
(def vd (partial vect_func / of_numbers))
(defn scalar [& vectors] 
	{:pre [(and (every? (fn [v] (is_vector v)) vectors)
				(every? (fn [v] (== (count (first vectors)) (count v))) vectors))]
	 :post [(number? %)]} 
	(apply + (apply v* vectors)))
(defn lacing [f s a b] (- (* (f a) (s b)) (* (s a) (f b))))
(defn vect_mul [a, b] (vector (lacing second last a b) (lacing last first a b) (lacing first second a b)))
(defn vect [& vectors] 
	{:pre [(and (every? (fn [v] (is_vector v)) vectors)
				(every? (fn [v] (== 3 (count v))) vectors))]
	 :post [(and (is_vector %) (== 3 (count %)))]}
	(reduce vect_mul vectors))
(defn v*s [v, & scalars] 
	{:pre [(and (is_vector v)
				(every? (fn [a] (number? a)) scalars))]
	 :post [(is_vector %)]}
	(def scl (apply * scalars))
	(mapv (fn [a] (* a scl)) v)) 
(def m+ (partial vect_func v+ of_vectors))
(def m- (partial vect_func v- of_vectors))
(def m* (partial vect_func v* of_vectors))
(def md (partial vect_func vd of_vectors))
(defn m*s [m, & scalars] 
	{:pre [(and (is_matrix m) (every? (fn [a] (number? a)) scalars))]
	 :post [(is_matrix %)]}
	(def scl (apply * scalars))
	(mapv (fn [v] (v*s v scl)) m))
(defn m*v [m, v]
	{:pre [(and (is_matrix m) (is_vector v))]
	 :post [(is_vector %)]}
 	(mapv (fn [a] (scalar a v)) m))
(defn transpose [m] 
	{:pre [(is_matrix m)]
	 :post [(is_matrix %)]}
	(apply mapv vector m))
(defn matrix_mul [a, b] 
	{:pre [(and (is_matrix a) (is_matrix b)
				(every? (fn [va] (== (count va) (count b))) a))]
	 :post [(and (is_matrix %) (== (count %) (count a)) (every? (fn [v] (every? (fn [vb] (== (count v) (count vb))) b)) %))]}
	(def btr (transpose b)) (mapv (partial (fn [m, v] (m*v m v)) btr) a))
(defn m*m [& matrixs] (reduce matrix_mul matrixs))

(defn dimensions [t] (if (or (number? t) (zero? (count t))) (list) (conj (dimensions (first t)) (count t))))
(defn check_dim [t dim] (or (zero? (count (dimensions t))) (every? true? (map == (reverse (dimensions t)) (reverse dim)))))
(defn max_dim_count [tensors] (apply max (mapv count (mapv dimensions tensors))))
(defn max_dim [tensors] (dimensions (first (filter (fn [t] (= (count (dimensions t)) (max_dim_count tensors))) tensors))))
(defn broadcast [d, t] 
	{:pre [(check_dim t d)]}
	(if (< (count (dimensions t)) (count d))  
		(let [cnt (nth d (- (count d) (count (dimensions t)) 1))] 
			(let [new_tens (vec (take cnt (cycle (vector t))))] (broadcast d new_tens)))
 		(identity t)))

(defn tf [f fnum & tensors] 
	(if (is_vector (first tensors)) (apply f tensors) 
		(if (number? (first tensors)) (apply fnum tensors) (apply mapv (partial tf f fnum) tensors))))

(defn simply_tfunc [fnum, f, & tensors] 
	(apply (partial tf f fnum) (mapv (fn [t] (broadcast (max_dim tensors) t)) tensors)))

(def hb+ (partial simply_tfunc + v+))
(def hb- (partial simply_tfunc - v-))
(def hb* (partial simply_tfunc * v*))
(def hbd (partial simply_tfunc / vd))