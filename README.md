# arborist

A Clojure library for walking and modifying nested data structures,
such as code trees.

See the org.timmc.arborist namespace for details.

```clojure
(require '[org.timmc.arborist :as tree])
;;= nil

(tree/walk #(cond (integer? %) inc
                  (string? %) count)
   {:int 4, :double 4.5, "key" [:even :in "here"]})
;;= {:int 5, :double 4.5, 3 [:even :in 4]}
```

Not yet released.

Compatible with Clojure 1.2.0 through 1.5.0.

## License

Copyright © 2013 Tim McCormack

Distributed under the Eclipse Public License, the same as Clojure.
