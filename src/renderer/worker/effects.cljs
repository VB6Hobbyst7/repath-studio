(ns renderer.worker.effects
  (:require
   [config :as config]
   [re-frame.core :as rf]
   [renderer.worker.events :as-alias worker.events]))

(rf/reg-fx
 ::post
 (fn [{:keys [data on-success on-error]}]
   (let [worker (js/Worker. "js/worker.js")
         id (uuid (:id data))]
     (.addEventListener worker "message"
                        #(let [data (-> (.. % -data)
                                        (js->clj :keywordize-keys true))]
                           (rf/dispatch [::worker.events/message
                                         id on-success data])
                           (.terminate worker)))

     ;; Worker on dev mode includes devtools and throw a `window is not defined`
     ;; error on init that terminates the worker.
     (.addEventListener worker "error"
                        #(do (rf/dispatch [::worker.events/message
                                           id on-error %])
                             (when-not config/debug?
                               (.terminate worker))))

     (.postMessage worker (clj->js data)))))
