;;; Code from Paradigms of AI Programming
;;; Copyright (c) 1991 Peter Norvig

(in-package :cm)

;; from auxfns.lisp

(defun mappend (fn list)
  "Append the results of calling fn on each element of list.
  Like mapcon, but uses append instead of nconc."
  (apply #'append (mapcar fn list)))

;; from simple.lisp

(defun random-elt (choices)
  "Choose an element from a list at random."
  (elt choices (random (length choices))))
