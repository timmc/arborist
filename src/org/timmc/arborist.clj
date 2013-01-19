(ns org.timmc.arborist)

(defn walk
  "Walk a Clojure data structure recursively. The dispatch function
is called on composite and atomic data structures. If it returns
nil/false for a form, descent continues (if possible). If it instead
returns a transformer function, that transformer is called on the form
and its results are used to replace the form.

Descent does not continue into the results of a replacement; it is up to
the transformer to call the walk function as necesary on child nodes.

To prevent descent without modifying a node, the dispatch function may
return the identity function."
  [dispatch form]
  (if-let [transform (dispatch form)]
    (transform form)
    (cond
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

     (coll? form)
     (into (empty form) (map (partial walk dispatch) form))

     :else ;; unknown type, don't know how to recurse
     form)))
