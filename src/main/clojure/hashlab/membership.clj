(ns hashlab.membership
  (:refer-clojure :exclude [assert get merge])
  (:import
    [java.util AbstractSet HashSet]
    [com.clearspring.analytics.stream.membership BloomFilter]))

;;; AMS = Approximate Membership Query

(defprotocol AMQString
  (add! [amqs ^String s])
  (maybe-contains? [ amqs ^String s])
  (merge [ & amqs]))

(extend-protocol AMQString

  AbstractSet
  (add! [ set ^String s]
    (.add set s)
    set)
  (maybe-contains? [set ^String s]
    (.contains set s))

  BloomFilter
  (add! [ bf ^String s]
    (. bf add s)
    bf)
  (maybe-contains? [ bf ^String s]
    (. bf isPresent s)))

(defn create-hash-set []
  (java.util.HashSet.))

(defn create-bloom-filter [ n m ]
  (BloomFilter. n m))
