(ns catvec.rvec)

(deftype RVec [meta ci i ^:mutable __hash]
  ISequential

  Object
  (toString [coll]
    (pr-str* coll))

  ICloneable
  (-clone [_]
    (RVec. meta ci i __hash))

  IWithMeta
  (-with-meta [coll meta]
    (RVec. meta ci i __hash))

  IMeta
  (-meta [_] meta)

  IStack
  (-peek [_] (-peek ci))
  (-pop [_]
    (if (== (count ci) 0)
      (throw (js/Error. "Can't pop empty vector"))
      (RVec. meta (-pop ci) (dec i) __hash)))
  
  ISeqable
  (-seq [coll])
  
  ISeq
  (-first [_] (-nth ci i))
  (-rest [coll]
    (if (pos? i)
      (RVec. ci (dec i) nil)))

  INext
  (-next [coll] (next (-seq coll)))

  ICounted
  (-count [_] (inc i))
  
  ICollection
  (-conj [coll o]
    (RVec. meta (inc cnt) cnt1 (inc cnt2) v1 (-conj v2 o) __hash))

  IPrintWithWriter
  (-pr-writer [coll writer opts]
    (pr-sequential-writer writer pr-writer "[" " " "]" opts coll))

  IIndexed
  (-nth [coll n]
    (cond
      (and (>= n 0) (< n cnt1)) (-nth v1 n)
      (and (>= n 0) (< n cnt)) (-nth v2 (- n cnt1))
      :else
      (throw (js/Error. (str "No item " n " in vector of length " cnt)))))
  (-nth [coll n not-found]
    (cond
      (and (>= n 0) (< n cnt1)) (-nth v1 n not-found)
      (and (>= n 0) (< n cnt)) (-nth v2 (- n cnt1) not-found)
      :else not-found))

  ILookup
  (-lookup [coll k] (-nth coll k nil))
  (-lookup [coll k not-found] (-nth coll k not-found))

  IAssociative
  (-assoc [coll k v]
    (cond
      (and (<= 0 k) (< k cnt2))
      (RVec. meta cnt cnt1 cnt2 (-assoc v1 k v) v2 __hash)
      (and (<= cnt1 k) (< k cnt))
      (RVec. meta cnt cnt1 cnt2 v1 (-assoc v2 k v) __hash)
      (== k cnt) (-conj coll v)
      :else
      (throw (js/Error. (str "Index " k "out of bounds  [0," cnt "]")))))

  IVector
  (-assoc-n [coll n val] (-assoc coll n val))

  IReduce
  (-reduce [v f]
    (ci-reduce v f))
  (-reduce [v f start]
    (ci-reduce v f start))

  IKVReduce
  (-kv-reduce [v f init]
    (-kv-reduce (into v1 v2) f init))

  IFn
  (-invoke [coll k]
    (-nth coll k))
  (-invoke [coll k not-found]
    (-nth coll k not-found))

  IReversible
  (-rseq [coll]
    (if (pos? cnt)
      (RSeq. coll (dec cnt) nil)))

  IEmptyableCollection
  (-empty [coll] (with-meta cljs.core.PersistentVector.EMPTY meta))

  IEquiv
  (-equiv [coll other] (equiv-sequential coll other))

  IHash
  (-hash [coll] (caching-hash coll hash-coll __hash)))
