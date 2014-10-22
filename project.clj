(defproject hashlab "0.1.0-SNAPSHOT"

  ; GENERAL OPTIONS

  :description "description"
  :url "url"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :aot :all
  :omit-source true

  ;; Options used by Java
  ;;; run with assertions enabled
  :jvm-opts ["-ea"]
 
  ; DEPENDENCIES

  :dependencies [
    [org.clojure/clojure "1.6.0"]
 
    ;; Runtime assertions
    [pjstadig/assertions "0.1.0"]

    ;; Commit 274 is used for stream-lib
    ;; 1a2799fbff3ef0426fdaadd37e4c744eeaeff908 Merge pull request #84 
    [com.clearspring.analytics/stream "2.8.0-SNAPSHOT"]]
  
  ; SOURCE DIRECTORY RECONFIGURATION

  :source-paths ["src/main/clojure"]
  :java-source-paths ["src/main/java"]
  :test-paths ["src/test/clojure"]
  
  ; PLUGINS + CONFIGURATION

  :plugins [ [codox "0.8.10"] ]

  ;; codox configuration

  :codox {
          :output-dir "target/apidoc"
          :sources [ "src/main/clojure"]
          :defaults {:doc/format :markdown}
          ;; Uncomment this to get github links in sources
          ;; :src-dir-uri "githubrepo/blob/master/"
          ;; :src-linenum-anchor-prefix "L"
          }
)
