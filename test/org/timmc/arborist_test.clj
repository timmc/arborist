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
               [[:hi]] :is-a-vec}
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
                   [[:bye]] :is-a-vec}))
    (is (instance? Rec (:rec output)))
    (is (= (meta (:sets output)) {:meta :data}))))
