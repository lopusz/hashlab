(ns hashlab.freq-test
  (:require [clojure.test :refer :all]
            [hashlab.freq :as hf]))

(deftest simple-test
  (let [
          cms1 (-> (hf/new-count-min-sketch 0.01 0.99 123)
                 (hf/add! "ala")
                 (hf/add! "ela")
                 (hf/add! "ula"))
        ]
  (is (= 1 (hf/get cms1 "ala")))
  (is (= 1 (hf/get cms1 "ula")))
  (is (= 1 (hf/get cms1 "ela")))))
