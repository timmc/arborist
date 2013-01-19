# arborist

A Clojure library for walking and modifying nested data structures,
such as code trees.

See the org.timmc.arborist namespace for details.

## Example

```clojure
(require '[org.timmc.arborist :as tree])
;;= nil

(tree/walk #(cond (integer? %) inc
                  (string? %) count)
   {:int 4, :double 4.5, "key" [:even :in "here"]})
;;= {:int 5, :double 4.5, 3 [:even :in 4]}
```

## Get it

Leiningen dependency:

```clojure
[org.timmc/arborist "1.0.0"]
```

Compatible with Clojure 1.2.0 through 1.5.0.

## Changelog

### v1.0.0

* First release
* Descends into lists, seqs, maps, sets, vectors, and records
* Metadata-preserving

## Building

Built with Leiningen 2, although Leiningen 1 will almost certainly work as well.

## License

Copyright © 2013 Tim McCormack

Distributed under the Eclipse Public License, the same as Clojure.
