(ns catvec.core
    (:require [clojure.browser.repl]
              [clojure.core.rrb-vector :as rrb])
    (:require-macros [catvec.core :refer [splice cat-array transient-array
                                          flatten-nodes]]))

;; (enable-console-print!)

;; (deftype Catvec [meta cnt cnt1 cnt2 v1 v2 ^:mutable __hash]
;;   ISequential

;;   Object
;;   (toString [coll]
;;     (pr-str* coll))

;;   ICloneable
;;   (-clone [_]
;;     (Catvec. meta cnt cnt1 cnt2 v1 v2 __hash))

;;   IWithMeta
;;   (-with-meta [coll meta]
;;     (Catvec. meta cnt cnt1 cnt2 v1 v2 __hash))

;;   IMeta
;;   (-meta [_] meta)

;;   IStack
;;   (-peek [_]
;;     (or (-peek v2) (-peek v1)))
;;   (-pop [_]
;;     (cond
;;       (and (== cnt1 0) (== cnt2 0))
;;       (throw (js/Error. "Can't pop empty vector"))
;;       (== cnt2 0)
;;       (Catvec. meta (dec cnt) (dec cnt1) cnt2 (-pop v1) v2 __hash)
;;       :else (Catvec. meta (dec cnt) cnt1 (dec cnt2) v1 (-pop v2) __hash)))
  
;;   ISeqable
;;   (-seq [coll]
;;     (seq (into v1 v2)))
  
;;   ISeq
;;   (-first [_]
;;     (or (first v1) (first v2)))
;;   (-rest [coll]
;;     (rest (-seq coll)))

;;   INext
;;   (-next [coll]
;;     (next (-seq coll)))

;;   ICounted
;;   (-count [_] cnt)
  
;;   ICollection
;;   (-conj [coll o]
;;     (Catvec. meta (inc cnt) cnt1 (inc cnt2) v1 (-conj v2 o) __hash))

;;   IPrintWithWriter
;;   (-pr-writer [coll writer opts]
;;     (pr-sequential-writer writer pr-writer "[" " " "]" opts coll))

;;   IIndexed
;;   (-nth [coll n]
;;     (cond
;;       (and (>= n 0) (< n cnt1)) (-nth v1 n)
;;       (and (>= n 0) (< n cnt)) (-nth v2 (- n cnt1))
;;       :else
;;       (throw (js/Error. (str "No item " n " in vector of length " cnt)))))
;;   (-nth [coll n not-found]
;;     (cond
;;       (and (>= n 0) (< n cnt1)) (-nth v1 n not-found)
;;       (and (>= n 0) (< n cnt)) (-nth v2 (- n cnt1) not-found)
;;       :else not-found))

;;   ILookup
;;   (-lookup [coll k] (-nth coll k nil))
;;   (-lookup [coll k not-found] (-nth coll k not-found))

;;   IAssociative
;;   (-assoc [coll k v]
;;     (cond
;;       (and (<= 0 k) (< k cnt2))
;;       (Catvec. meta cnt cnt1 cnt2 (-assoc v1 k v) v2 __hash)
;;       (and (<= cnt1 k) (< k cnt))
;;       (Catvec. meta cnt cnt1 cnt2 v1 (-assoc v2 (- k cnt1) v) __hash)
;;       (== k cnt) (-conj coll v)
;;       :else
;;       (throw (js/Error. (str "Index " k "out of bounds  [0," cnt "]")))))

;;   IVector
;;   (-assoc-n [coll n val]
;;     (assoc coll n val))

;;   IReduce
;;   (-reduce [v f]
;;     (ci-reduce v f))
;;   (-reduce [v f start]
;;     (ci-reduce v f start))

;;   IKVReduce
;;   (-kv-reduce [v f init]
;;     (-kv-reduce (into v1 v2) f init))

;;   IFn
;;   (-invoke [coll k]
;;     (-nth coll k))
;;   (-invoke [coll k not-found]
;;     (-nth coll k not-found))

;;   IReversible
;;   (-rseq [coll]
;;     (if (pos? cnt)
;;       (RSeq. coll (dec cnt) nil)))

;;   IEmptyableCollection
;;   (-empty [coll] (with-meta cljs.core.PersistentVector.EMPTY meta))

;;   IEquiv
;;   (-equiv [coll other] (equiv-sequential coll other))

;;   IHash
;;   (-hash [coll] (caching-hash coll hash-coll __hash)))

;; (deftype Catvec [meta cnt shift root tail ^:mutable __hash]
;;   ISequential

;;   Object
;;   (toString [coll]
;;     (pr-str* coll))

;;   ICloneable
;;   (-clone [_]
;;     (Catvec. meta cnt shift root tail __hash))

;;   IWithMeta
;;   (-with-meta [coll meta]
;;     (Catvec. meta cnt shift root tail __hash))

;;   IMeta
;;   (-meta [_] meta)

;;   IStack
;;   (-peek [_]
;;     (or (-peek v2) (-peek v1)))
;;   (-pop [_]
;;     (cond
;;       (and (== cnt1 0) (== cnt2 0))
;;       (throw (js/Error. "Can't pop empty vector"))
;;       (== cnt2 0)
;;       (Catvec. meta shift root tail __hash)
;;       :else (Catvec. meta shift root tail __hash)))
  
;;   ISeqable
;;   (-seq [coll]
;;     (seq (into v1 v2)))
  
;;   ISeq
;;   (-first [_]
;;     (or (first v1) (first v2)))
;;   (-rest [coll]
;;     (rest (-seq coll)))

;;   INext
;;   (-next [coll]
;;     (next (-seq coll)))

;;   ICounted
;;   (-count [_] cnt)
  
;;   ICollection
;;   (-conj [coll o]
;;     (Catvec. meta shift root tail __hash))

;;   IPrintWithWriter
;;   (-pr-writer [coll writer opts]
;;     (pr-sequential-writer writer pr-writer "[" " " "]" opts coll))

;;   IIndexed
;;   (-nth [coll n]
;;     (cond
;;       (and (>= n 0) (< n cnt1)) (-nth v1 n)
;;       (and (>= n 0) (< n cnt)) (-nth v2 (- n cnt1))
;;       :else
;;       (throw (js/Error. (str "No item " n " in vector of length " cnt)))))
;;   (-nth [coll n not-found]
;;     (cond
;;       (and (>= n 0) (< n cnt1)) (-nth v1 n not-found)
;;       (and (>= n 0) (< n cnt)) (-nth v2 (- n cnt1) not-found)
;;       :else not-found))

;;   ILookup
;;   (-lookup [coll k] (-nth coll k nil))
;;   (-lookup [coll k not-found] (-nth coll k not-found))

;;   IAssociative
;;   (-assoc [coll k v]
;;     (cond
;;       (and (<= 0 k) (< k cnt2))
;;       (Catvec. meta cnt shift root tail __hash)
;;       (and (<= cnt1 k) (< k cnt))
;;       (Catvec. meta cnt shift root tail __hash)
;;       (== k cnt) (-conj coll v)
;;       :else
;;       (throw (js/Error. (str "Index " k "out of bounds  [0," cnt "]")))))

;;   IVector
;;   (-assoc-n [coll n val]
;;     (-assoc coll n val))

;;   IReduce
;;   (-reduce [v f]
;;     (ci-reduce v f))
;;   (-reduce [v f start]
;;     (ci-reduce v f start))

;;   IKVReduce
;;   (-kv-reduce [v f init]
;;     (-kv-reduce (into v1 v2) f init))

;;   IFn
;;   (-invoke [coll k]
;;     (-nth coll k))
;;   (-invoke [coll k not-found]
;;     (-nth coll k not-found))

;;   IReversible
;;   (-rseq [coll]
;;     (if (pos? cnt)
;;       (RSeq. coll (dec cnt) nil)))

;;   IEmptyableCollection
;;   (-empty [coll] (with-meta cljs.core.PersistentVector.EMPTY meta))

;;   IEquiv
;;   (-equiv [coll other] (equiv-sequential coll other))

;;   IHash
;;   (-hash [coll] (caching-hash coll hash-coll __hash)))

(defn catvec
  ([] [])
  ([v1] v1)
  ([v1 v2]
     (let [cnt1 (count v1)
           cnt2 (count v2)
           cnt (+ cnt1 cnt2)
           arr (make-array cnt1)
           tail (.-tail v1)]
       (apply #(.splice arr 0 (- 32 (alength tail)) %&) tail)
       arr))

            ;;  (cljs.core.PersistentVector.fromArray
            ;; (.concat (.concat a1 (array-for v1 0))
            ;;          (.concat a2 (array-for v2 0))))
  ([v1 v2 v3]
     (catvec (catvec v1 v2) v3))
  ([v1 v2 v3 v4]
     (catvec (catvec v1 v2) (catvec v3 v4)))
  ([v1 v2 v3 v4 & vn]
     (catvec (catvec (catvec v1 v2) (catvec v3 v4)) (apply catvec vn))))

(defn ^:export -main []
  (simple-benchmark
    [v1 (vec (range 50000))
     v2 (vec (range 50000 100000))
     v3 (vec (range 100000))]
    (-> (catvec v1 v2)
        (subvec 0 100)
        (catvec v3)
        (subvec 100))
    10000)

  (simple-benchmark
    [v1 (vec (range 50000))
     v2 (vec (range 50000 100000))
     v3 (vec (range 100000))]
    (-> (into v1 v2)
        (subvec 0 100)
        (into v3)
        (subvec 100))
    1)
  
  (simple-benchmark
    [v1 (rrb/vec (range 50000))
     v2 (rrb/vec (range 50000 100000))
     v3 (rrb/vec (range 100000))]
    (-> (rrb/catvec v1 v2)
        (rrb/subvec 0 100)
        (rrb/catvec v3)
        (rrb/subvec 100))
    10000))

;; (def v1 (vec (range 50000)))
;; (def v2 (vec (range 50000 100000)))
(def v1 (vec (range 1000)))
(def v2 (vec (range 1000 2000)))

