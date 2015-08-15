(ns mutils.fn.compose)

(defmacro defboolcomp [name macro-name fn-name]
  `(defn ~name
     ([f#] f#)
     ([f# g#]
        (fn ([arg#] (~macro-name (f# arg#) (g# arg#)))))
     ([f# g# h#]
        (fn [arg#]
          (~macro-name (f# arg#) (g# arg#) (h# arg#))))
     ([f# g# h# & fns#]
        (fn [arg#]
          (~fn-name #(% arg#) (list* f# g# h# fns#))))))

(defboolcomp and-comp and every?)
(defboolcomp or-comp or some)
(defboolcomp xor-comp bit-xor
  (fn [call-fn fns]
    (apply bit-xor (map call-fn fns))))

(defn some-comp
  ([f] f)
  ([f g]
     (fn [arg] (some-> arg g f)))
  ([f g h]
     (fn [arg] (some-> arg h g f)))
  ([f g h & fns]
     (fn [arg]
       (let [i (apply some-comp fns)]
        (some-> arg i h g f)))))

(defmacro comp' [& exprs]
  `(comp ~@(map
            (fn [form]
              (if (list? form)
                (list* clojure.core/partial form)
                form))
            exprs)))
