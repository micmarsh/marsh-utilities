# Marsh Utilities

Various super-general Clojure utilities I've found useful at one point or another. Will probably get a less self-centered name if this is useful anywhere else.

## Usage
`[mutils "0.1.0-SNAPSHOT"]`

## Macros
### Better "case" macro
Ever been frustrasted by the way `case` only accepts literals? Try `mutils.macro.control/case`!

```clojure
  (def one 1)
  (def two 2)

  (clojure.core/case 1
     one "one"
     two "two"
     (inc 2) "three"
     4 "four") ; => throws IllegalArgumentException

  (mutils.macro.control/case 1
     one "one"
     two "two"
     (inc two) "three"
     4 "four") ; => "one"

  (mutils.macro.control/case 3
     one "one"
     two "two"
     (inc two) "three"
     4 "four") ; => "three"
```


## Function Composition
`mutils.fn.compose` features many variations on `comp`

### Predicate Compositions
```clojure
;; instead of
(defn person? [map]
  (and (:name map) (:address map)))
;; try
(def person? (and-comp :name :address))

;; instead of
(defn odd-or-neg? [num]
  (or (odd? num) (neg? num)))
;; use
(def odd-or-neg? (or-comp odd? neg?))
```

### Nil-Safe Composition
```clojure
;; instead of
(defn safe-chain [x]
  (some-> x
          (unsafe-fn1)
          (unsafe-fn3)
          (unsafe-fn3)))
;; use
(def safe-chain
  (some-comp unsafe-fn3 unsafe-fn2 unsafe-fn1))
```

### Composition w/ Haskell-ish Currying
```clojure
;; for those tired of typing this sort of thing
(def total-programmer-ages
  (comp (partial reduce +)
        (partial map :age)
        (partial filter programmer?)))
;; a handy macro
(def total-programmer-ages
  (comp' (reduce +)
         (map :age)
         (filter programmer?)))
```
Transducers are probably better than that^, but whatever.


## Lazier Sequence Operation(s)
Although it returns a lazy sequence, `mapcat` is not as lazy as it could be (more detail [here](http://stackoverflow.com/questions/21943577/mapcat-breaking-the-lazyness)), so `mutils.seq.lazy` provides a new `mapcat` that won't force its sequence argument.

```clojure
(defn hello []
  (repeatedly 3 #(do (println "hello") "hello")))

(def result (clojure.core/mapcat identity (hello)))
;; prints "hello" three times, assigns result

(def result (mutils.seq.lazy/mapcat identity (hello)))
;; doesn't print anything, assigns result

(first result) ;; => \h
;; prints "hello" only once
```

## Tree Utilities

### Breadth-First
Normal `tree-seq`s use depth-first traversal, so `mutils.seq.tree/tree-seq` allows you to optionally specify a breadth-first traversal:
```clojure
;; Example data from http://clojuredocs.org/clojure.core/tree-seq#example-542692ccc026201cdc326c82

(tree-seq seq? identity '((1 2 (3)) (4)))
;; => (((1 2 (3)) (4)) (1 2 (3)) 1 2 (3) 3 (4) 4)

(tree-seq {:breadth true} seq? identity '((1 2 (3)) (4)))
;; => (((1 2 (3)) (4)) (1 2 (3)) (4) 1 2 (3) 4 3)
```
### Path
If you're searching a tree for something in particular, you may find it helpful to remember the path to each node as you enumerate it
```clojure
(tree-seq seq? identity '((1 (3)) 4))
;; => (((1 (3)) 4) (1 (3)) 1 (3) 3 4)

(tree-seq {:path true} seq? identity '((1 (3)) 4))
;;({:path [], :node ((1 (3)) 4)}
;; {:path [((1 (3)) 4)], :node (1 (3))}
;; {:path [((1 (3)) 4), (1 (3))], :node 1}
;; {:path [((1 (3)) 4), (1 (3))], :node (3)}
;; {:path [((1 (3)) 4), (1 (3)), (3)], :node 3}
;; {:path [((1 (3)) 4)], :node 4})
```

## License

Everything possible is public domain
