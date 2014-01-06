(ns catvec.core
  (:require [cljs.core :as core]
            [cljs.compiler]
            [cljs.env :as env]
            [cljs.analyzer :as ana]
            [clojure.core :as c]))

(defmacro splice
  [arr idx n & xs]
  `(.apply js/Array.prototype.splice ~arr (core/array ~idx ~n ~@xs)))

(defmacro cat-array
  [arr1 arr2]
  `(splice ~arr1 (core/alength ~arr1) (core/alength ~arr2) ~@arr2))

(defmacro transient-array
  [v1 v2]
  `(let [v1# (core/-as-transient ~v1) v2# (core/-as-transient ~v2)
         r1# (.-root v1#) r2# (.-root v2#)
         t1# (.-tail v1#) t2# (.-tail v2#)
         s1# (.-shift v1#) s2# (.-shift v2#)
         c1# (.-cnt v1#) c2# (.-cnt v2#)
         arr# (int-array 32 (iterate #(+ 32 %) 0))]
     (areduce arr# idx# ret# arr#
       (aset arr# idx# (core/editable-array-for v1# (aget arr# idx#))))))

(defmacro flatten-nodes
  [v]
  `(let [v# (core/-as-transient ~v)
         len# (count v#)
         arr# (core/make-array len#)]
     (loop [idx# 0]
       (when (< (* idx# 32) len#)
         (apply #(splice ret# idx# 32 %&) (core/editable-array-for v# idx#))
         (recur (+ idx# 32))))
     arr#))
