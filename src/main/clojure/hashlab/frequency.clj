(ns hashlab.frequency
  (:refer-clojure :exclude [assert get merge])
  (:require
     [ pjstadig.assertions :refer [assert]])
  (:import [com.clearspring.analytics.stream.frequency CountMinSketch]))

;; From CountMinSketch sources:
;; The estimate is correct within 'epsilon' * (total item count),
;; * with probability 'confidence'.


(defprotocol ApproxStringFrequencyCounter
  (add-str! [ asfc ^String s]
            [ asfc ^String s ^Long count])
  (approx-count-str [asfc ^String s])
  (merge-internal [first-asfc rest-asfcs]))

(extend-protocol ApproxStringFrequencyCounter
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

(defn create-count-min-sketch [ ^double epsOfTotalCount
                                ^double confidence
                                ^Integer seed]
  (CountMinSketch.  epsOfTotalCount confidence seed))
