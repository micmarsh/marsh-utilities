(ns mutils.seq.lazy
  (:refer-clojure :exclude [mapcat]))

(defn mapcat
  "Like `clojure.core/mapcat`, won't evaluate its seq argument
  Compare:
    (defn hello []
      (repeatedly 3 #(do (println \"hello\") \"hello\")))
    (def result (clojure.core/mapcat identity (hello)))
    ;; prints \"hello\" three times, assigns result
    (def result (mutils.seq.lazy/mapcat identity (hello)))
    ;; doesn't print anything, assigns result
    (first result) => \"hello\"
    ;; prints \"hello\" only once"
  [f coll]
  (for [item coll x (f item)] x))
