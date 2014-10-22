(ns hashlab.membership
  (:refer-clojure :exclude [merge])
  (:import
    [java.util AbstractSet HashSet]
    [com.clearspring.analytics.stream.membership Filter BloomFilter]))

;; IDEA Add support for Quotient Filter
;; http://en.wikipedia.org/wiki/Quotient_filter
;; http://stackoverflow.com/questions/12212931/is-there-an-open-source-implementation-of-the-quotient-filter
;; https://github.com/vedantk/quotient-filter

(defprotocol ApproxMembershipStringFilter
  (add-str! [amsf ^String s])
  (maybe-contains-str? [ amsf ^String s])
  (merge-internal [ amsf amsfs]))

(extend-protocol ApproxMembershipStringFilter
  AbstractSet
  (add-str! [set ^String s]
    (.add set s)
    set)
  (maybe-contains-str? [set ^String s]
    (.contains set s))
  (merge-internal [ first-set rest-sets ]
    (dorun
      (map
       #(.addAll first-set %) rest-sets))
     first-set)

  BloomFilter
  (add-str! [bf ^String s]
    (.add bf s)
    bf)
  (maybe-contains-str? [bf ^String s]
    (. bf isPresent s))
  (merge-internal [ first-bf rest-bfs ]
    (.merge first-bf
       (into-array Filter rest-bfs))))

(defn merge [ & sets ]
  (merge-internal (first sets) (rest sets)))

(defn create-hash-set []
  (HashSet.))

(defn create-bloom-filter [ n m ]
  (BloomFilter. n m))
