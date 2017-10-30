(ns sin-wave.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [sin-wave.core-test]
   [sin-wave.common-test]))

(enable-console-print!)

(doo-tests 'sin-wave.core-test
           'sin-wave.common-test)
