---
Usage

;; in your project's namespace
(:require [catvec.core :refer [catvec])

(def v1 (vec (range 50000)))
(def v2 (vec (range 50000 100000))

;; Use catvec whenever you want to concatenate vectors.
;; in idiomatic Clojure you'd ususally concatante vectors like this

(into v1 v2) ;; concatenate v1 and v2

;; but this gets expensive as your vectors grow in size. 

;; instead just use catvec in place of into

(catvec v1 v2)

;; the resulting vector will work exactly like a vector, can be subvec'd in constant time,
;; and all of the other vector goodies. (constant time reversal, constant access from tail with peek, etc.)

Enjoy!



