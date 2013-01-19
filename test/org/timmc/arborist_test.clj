(ns org.timmc.arborist-test
  (:use clojure.test
        org.timmc.arborist))

;; TODO: Can't safely dissoc basis keys?
#_(defrecord Rec [a b])

(deftest comprehensive
  (let [thing {#_#_:rec (assoc (Rec. 1 2) :c 3)
               nil 17
               5 :protect
               :protect 6
               9 :increment
               [[:hi]] :is-a-vec}
        protected-entry? #(when (instance? java.util.Map$Entry %)
                            (or (= (key %) :protect)
                                (= (val %) :protect)))
        munge #(cond (number? %) inc
                     (= % :hi) (constantly :bye)
                     (protected-entry? %) identity)
        actual (walk munge thing)
        expected {#_#_:rec (assoc (Rec. 2 3) :c 4)
                  nil 18
                  5 :protect
                  :protect 6
                  10 :increment
                  [[:bye]] :is-a-vec}]
    (is (= actual expected))))
