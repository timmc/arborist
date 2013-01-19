(ns org.timmc.arborist-test
  (:use clojure.test
        org.timmc.arborist))

(defrecord Rec [a b])

(deftest pretty-comprehensive
  (let [input {:rec (assoc (Rec. 1 2) :c 3)
               nil 17
               5 :protect
               :protect 6
               9 :increment
               :sets (with-meta #{:foo 5} {:meta :data})
               [[:hi] :foo] :is-a-vec}
        protected-entry? #(when (instance? java.util.Map$Entry %)
                            (or (= (key %) :protect)
                                (= (val %) :protect)))
        munge #(cond (number? %) inc
                     (= % :hi) (constantly :bye)
                     (protected-entry? %) identity)
        output (walk munge input)]
    (is (= output {:rec (assoc (Rec. 2 3) :c 4)
                   nil 18
                   5 :protect
                   :protect 6
                   10 :increment
                   :sets #{:foo 6}
                   [[:bye] :foo] :is-a-vec}))
    (is (instance? Rec (:rec output)))
    (is (= (meta (:sets output)) {:meta :data}))))

(deftest guarding
  (is (= (walk #(when (integer? %) inc) [(take 3 (range)) '(10 11)])
         '[(1 2 3) (11 12)]))
  (let [inf (range)
        in [inf 4]
        out (walk #(cond (instance? clojure.lang.LazySeq %) identity
                         (integer? %) inc)
                  in)]
    (is (= out [inf 5]))))
