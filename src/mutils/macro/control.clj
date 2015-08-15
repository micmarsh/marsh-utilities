(ns mutils.macro.control
  (:refer-clojure :exclude [case]))

(defn- eval-form [[sym result]]
  [(cond (symbol? sym) (.getRawRoot (resolve sym))
         (list? sym) (eval sym)
         :else sym)
   result])

(defmacro case [item & forms]
  "An extended version of clojure.core/case which allows non-literal conditions.

  Works by resolving all symbols and lists to their literal values where possible.
  Will result in expected behavior where a form can't be fully resolved.
  Examples:
  (def one 1)
  (def two 2)

  (clojure.core/case 1
     one \"one\"
     two \"two\"
     (inc 2) \"three\"
     4 \"four\") => throws IllegalArgumentException

  (mutils.macro.control/case 1
     one \"one\"
     two \"two\"
     (inc two) \"three\"
     4 \"four\") => \"one\"

  (mutils.macro.control/case 3
     one \"one\"
     two \"two\"
     (inc two) \"three\"
     4 \"four\") => \"three\""
  {:pre [(even? (count forms))]}
  (let [resolved (mapcat eval-form (partition 2 forms))]
    `(clojure.core/case ~item ~@resolved)))
