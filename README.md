# Marsh Utilities

Various super-general Clojure utilities I've found useful at one point or another. Will probably get a less self-centered name if this is useful anywhere else.

## Usage
`[mutils "0.1.0-SNAPSHOT"]`

## Lazier Sequence Operation(s)
Although it returns a lazy sequence, `mapcat` is not as lazy as it could be (more detail [here](http://stackoverflow.com/questions/21943577/mapcat-breaking-the-lazyness)), so `mutils.seq.lazy` provides a new `mapcat` that won't force its sequence argument.

```clojure
(defn hello []
  (repeatedly 3 #(do (println \"hello\") \"hello\")))

(def result (clojure.core/mapcat identity (hello)))
;; prints \"hello\" three times, assigns result

(def result (mutils.seq.lazy/mapcat identity (hello)))
;; doesn't print anything, assigns result

(first result) ;; => \h
;; prints \"hello\" only once"
```

## Tree Utilities

### Breadth-First
Normal `tree-seq`s use depth-first traversal, so `mutils.seq.tree/tree-seq` allows you to optional specify a breadth-first traversal:
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
