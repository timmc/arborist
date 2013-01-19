(defproject org.timmc/arborist "0.1.0-SNAPSHOT"
  :description "Tree-walking with shortcutting."
  :url "https://github.com/timmc/arborist"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]]
  :profiles {:1.2.0 {:dependencies [[org.clojure/clojure "1.2.0"]]}
             :1.2.1 {:dependencies [[org.clojure/clojure "1.2.1"]]}
             :1.3.0 {:dependencies [[org.clojure/clojure "1.3.0"]]}
             :1.5 {:dependencies [[org.clojure/clojure "1.5.0-RC2"]]}}
  :aliases {"old" ["with-profile" "1.2.0:1.2.1:1.3.0"]})
