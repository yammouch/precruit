; echo aaabbccd | java clojure.main -i [this .clj file]

; Decrement nth element, increment (n-1)th element of a given vector.
; If the last element is 0, the 0 is thrown away.
(defn consume [n v]
  (as-> (update-in v [n] - 1) v
        (if (= (peek v) 0)
          (pop v)
          v)
        (if (< 0 n)
          (update-in v [(- n 1)] + 1)
          v)))

;(defn perm [exc v]
;  (+ (if (= (v 0) 0)
;       0
;       (* (- (v 0) (if (= exc 1) 1 0))
;          (perm 0 (consume 0 v))))
;     (if (= (v 1) 0)
;       0
;       (* (- (v 1) (if (= exc 2) 1 0))
;          (perm 1 (consume 1 v))))
;     (if (= (v 2) 0)
;       0
;       (* (- (v 2) (if (= exc 3) 1 0))
;          (perm 2 (consume 2 v))))
;     (if ...

(def perm-memo)

(defn perm [exc v]
  (if (empty? v)
    1
    (mod (apply + (map-indexed
                   (fn [i n]
                     (let [ndec (- n (if (= exc (+ i 1)) 1 0))]
                       (if (= ndec 0)
                         0
                         (* ndec
                            (perm-memo i (consume i v))
                            ))))
                   v))
         1000000007)))

(def perm-memo (memoize perm))

(println
 (perm-memo 0
  (as-> (read-line) x
        (group-by identity x)
        (map (comp count second) x)
        (let [nmax (apply max x)
              v (vec (repeat nmax 0))]
          (reduce (fn [v n] (update-in v [(- n 1)] + 1))
                  v x)))))
