(defclass shopcart-template business.rulesbeans.ShopCartBean)

(defmodule rules-shopcart)

(defrule shopcart-notempty
    (shopcart-template (isEmpty ?z) )
    (test (= ?z TRUE))
     =>    
    (throw (new rulesengine.ValidationException "To proceed to Checkout, your Shopping Cart must contain at least one item.")))
    
