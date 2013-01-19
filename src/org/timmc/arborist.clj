(ns org.timmc.arborist
  "Walk and modify data structures. API consists of the walk function.

Tips:

* To prevent descent without modifying a node, the dispatch function may
  return the identity function.
* To handle data structures that arborist doesn't know how to descend into,
  the dispatch function should return a transformer that calls the
  walk function manually. Remember to preserve metadata when implementing.
* Arborist does not normally descend into metadata, but a transformer could
  again do this manually.
* Seq-walking is lazy. To force seqs, add a dispatch clause such as
  `(seq? %) (comp doall (partial walk dispatch))")

(defn walk
  "Walk a data structure recursively. The dispatch function
is called on composite and atomic data structures. If it returns
nil/false for a form, descent continues (if possible). If it instead
returns a transformer function, that transformer is called on the form
and its results are used to replace the form.

Descent does not continue into the results of a replacement; it is up to
the transformer to call the walk function as necesary on child nodes.

Nota bene:
* Seqs are walked lazily.
* walk will not descend into sequential? values that do not pass one of
  list?, seq?, or vector?, since it cannot guarantee the ordering of the
  output.

Metadata and collection types are preserved."
  [dispatch form]
  (if-let [transform (dispatch form)]
    (transform form)
    (cond
     ;; unknown type, don't know how to recurse
     (not (coll? form))
     form

     ;; Maps are handled specially -- transformation of a KV pair
     ;; is really an assoc and/or dissoc.
     (map? form)
     (loop [out form
            pairs (seq form)]
       (if (seq pairs)
         (let [[k v :as kv] (first pairs)]
           (let [[k* v*] (if-let [transform (dispatch kv)]
                           (transform kv)
                           [(walk dispatch k) (walk dispatch v)])]
             (if (= k k*)
               ;; protect records by not dissoc'ing basis keys, when possible
               (recur (assoc out k v*)
                      (rest pairs))
               (recur (assoc (dissoc out k) k* v*)
                      (rest pairs)))))
         out))

     ;; Lists can't use #'into, since they'd get reversed.
     (list? form)
     (with-meta (apply list (map #(walk dispatch %) form))
       (meta form))

     ;; Seqs are handled lazily
     (seq? form)
     (with-meta (map #(walk dispatch %) form)
       (meta form))

     ;; Vectors can use #'into and #'empty since they conj to the end.
     ;; So can sets, since they aren't ordered.
     (or (vector? form)
         (not (sequential? form)))
     (with-meta (into (empty form) (map #(walk dispatch %) form))
       (meta form))

     ;; Can't safely process arbitrary sequential collections, since
     ;; we don't know if they conj to the end.
     :else
     form)))
