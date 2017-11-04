(ns sin-wave.core
;  (:require [om.core :as om :include-macros true]
;            [om.dom :as dom :include-macros true])
)

;(enable-console-print!)

;(defonce app-state (atom {:text "Hello Chestnut!"}))

;(defn root-component [app owner]
;  (reify
;    om/IRender
;    (render [_]
;      (dom/div nil (dom/h1 nil (:text app))))))

;(defn render []
;  (om/root
;   root-component
;   app-state
;   {:target (js/document.getElementById "app")}))

(def canvas (.getElementById js/document "myCanvas"))
(def ctx    (.getContext canvas "2d"))

(defn deg->rad
   [n]
   (* (/ Math/PI 180) n))

(defn sin-coord 
  [x]
  (let [sin (Math/sin (deg->rad x))
        y (- 100 (* sin 90))]
        { :x x
          :y y
          :sin sin}))

(def interval js/Rx.Observable.interval)
(def concat js/Rx.Observable.concat)
(def defer js/Rx.Observable.defer)
(def from-event js/Rx.Observable.fromEvent)

(def mouse-click (from-event canvas "click"))
(def time (interval 1))
(def sin-wave (.map time sin-coord))

(def red (.map time (fn [_] "red")))
(def blue (.map time (fn [_] "blue")))
(def color (.map sin-wave
            (fn [{:keys [sin]}]
              (if (< sin 0)
                "red"
                "blue"))))

(defn fill-rect [x y color]
  (set! (.-fillStyle ctx) color)
  (.fillRect ctx x y 3 3))

(def rainbow-color (.map time (fn [_] 
                                (let [n (inc (rand-int 6))]
                                  (condp = n
                                    1 "red"
                                    2 "orange"
                                    3 "yellow"
                                    4 "green"
                                    5 "blue"
                                    6 "indigo"
                                    7 "violet")))))

(def cycle-color
  (concat (.takeUntil red mouse-click)
          (defer #(concat (.takeUntil blue mouse-click)
                          cycle-color))))

(.clearRect ctx 0 0 (.-width canvas) (.-height canvas))
                
(-> (.zip sin-wave rainbow-color #(assoc % :color %2))
  (.take 600)
  (.subscribe (fn [{:keys [x y color]}]
      (fill-rect x y color)
      )))