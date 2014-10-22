(ns hashlab.freq
  (:refer-clojure :exclude [assert get merge])
  (:require
     [ pjstadig.assertions :refer [assert]])
  (:import [com.clearspring.analytics.stream.frequency CountMinSketch]))

;; From CountMinSketch sources:
;; The estimate is correct within 'epsilon' * (total item count),
;; * with probability 'confidence'.


(defn new-count-min-sketch [ ^double epsOfTotalCount
                             ^double confidence
                             ^Integer seed]
  (CountMinSketch.  epsOfTotalCount confidence seed))

(defn add!
  ([ ^CountMinSketch cms ^String s]
    (. cms add s 1)
    cms)
  ([ ^CountMinSketch cms ^String s ^Long count ]
    (. cms add s count)
    cms))


(defn get [ ^CountMinSketch cms ^String s]
  (. cms estimateCount s))

(defn merge [ & args ]
  (CountMinSketch/merge (into-array CountMinSketch args)))
