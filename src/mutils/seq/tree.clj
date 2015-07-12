(ns mutils.seq.tree
  (:refer-clojure :exclude [tree-seq mapcat])
  (:require [mutils.seq.lazy :refer [mapcat]]))

(defn- bf-tree-seq
  [branch? children root]
  (let [next (fn next [prev-nodes]
               (lazy-seq
                (when-not (empty? prev-nodes)
                  (let [branches (filter branch? prev-nodes)
                        kids (mapcat children branches)]
                    (concat kids (next kids))))))]
    (cons root (next [root]))))

(defn tree-seq
  "Like `clojure.core/tree-seq`, but with an optional first argument of custom options
   Options:
    breadth:
     When truthy, will traverse the tree breadth-first
    path:
     When truthy, will return a sequnce of maps {:node :path}, where :node is the value of the tree node, and :path is a vector of previous nodes on the traversal path

  Defaults to `clojure.core/tree-seq` when no options are given"
  ([branch? children root]
     (clojure.core/tree-seq branch? children root))
  ([{:keys [path breadth]}
    branch? children root]
     (let [func (if breadth bf-tree-seq tree-seq)]
       (if path
         (func (comp branch? :node)
               (fn [{:keys [node path]}]
                 (map (partial hash-map :path (conj path node) :node)
                      (children node)))
               {:node root :path []})
         (func branch? children root)))))
