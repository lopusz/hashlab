(ns hashlab.frequency
  (:refer-clojure :exclude [assert get merge])
  (:require
     [ pjstadig.assertions :refer [assert]])
  (:import
     [java.util AbstractMap HashMap ]
     [com.clearspring.analytics.stream.frequency CountMinSketch]))

(defprotocol ApproxStringFrequencyCounter
  (add-str! [ asfc ^String s]
            [ asfc ^String s ^Long count])
  (approx-count-str [asfc ^String s])
  (merge-internal [first-asfc rest-asfcs]))

(defn ^:private merge-to-first [ ^AbstractMap m1 ^AbstractMap m2]
  (dorun
    (map
      #(add-str! ^AbstractMap m1 % (.get m2 %))
      (.keySet m2))))

(extend-protocol ApproxStringFrequencyCounter
  AbstractMap
  (add-str!
    ([ a-map ^String s ^Long count ]
       (if-let [val (.get a-map s)]
         (.put a-map s (+ val count))
         (.put a-map s count))
       a-map)
    ([a-map ^String s]
      (add-str! a-map s 1)))
  (approx-count-str [ a-map ^String s]
     (if-let [ c (.get a-map s) ]
       c
       0))
  (merge-internal [first-map rest-maps]
    (let [res (.clone first-map) ]
      (dorun
        (map #(merge-to-first res %) rest-maps))
      res))

  CountMinSketch
  (add-str!
    ([ cms ^String s]
       (. cms add s 1)
       cms)
    ([ cms ^String s ^Long count ]
       (. cms add s count)
       cms))
  (approx-count-str [cms ^String s]
    (. cms estimateCount s))
  (merge-internal [ cms-first cms-rest ]
    (CountMinSketch/merge
      (into-array
        CountMinSketch
        (cons cms-first cms-rest)))))

(defn merge [ & args ]
  (merge-internal (first args) (rest args)))

(defn create-count-min-sketch
  "From CountMinSketch sources:
   The estimate is correct within 'epsOfTotalCount' * (total item count),
   with probability 'confidence'."
  [ ^double epsOfTotalCount ^double confidence ^Integer seed]
  (CountMinSketch.  epsOfTotalCount confidence seed))

(defn create-hash-map []
  (HashMap.))
