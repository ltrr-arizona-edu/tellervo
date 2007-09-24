(define (build . specs)
  (cond
   ((eq? 'button (car specs))
    (build-button (cdr specs)))
					; ...
   )
  )

; example:
;   (build '(button id: x label: "OK"))
;   => JButton x = new JButton("OK")

(define (build-button specs)
  (display "JButton ")
  (display (cdr (assoc 'id specs)))
  (display " = new JButton();")
  (newline)
  (display (cdr (assoc 'id specs)))
  (display ".setText(\"")
  (display (cdr (assoc 'text specs)))
  (display "\");")
  (newline))

;(build (button text: "ok"))

; (build 'button '((id . "x") (text . "OK")))
(build 'button '((id . "x") (text . "OK")))

;;
;; panels/layouts
;;
(panel id: p1
       layout: border
       n
       w c e
       s)
(panel id: p2
       layout: box <x/y>
(panel id: p3
       layout: flow <l/r/c>
       c1 c2 c3 ...)
(panel id: p4
       layout: grid <r,c>
       c1 c2 c3
       c4 c5 c6
       ...)

;;
;; dialogs/frames
;;
(dialog id: d1
	title: "dialog"
	(...))
(frame id: f1
       title: "frame"
       (...))

;;
;; buttons/labels/textareas
;;
(button id: blah
	text: "blah")
(label id: blah
       text: "blah")
(textarea id: blah
	  text: "blah"
	  rows: 4
	  columns: 90)

;;
;; menus/menuitems
;;
(menu id: blah
      text: "File" ; i18n - ???
      (menuitem ...)
      (menuitem ...))
(menuitem id: blah
	  text: "Open...") ; i18n - ???
(menubar id: blah
	 (menu ...)
	 (menu ...)
	 (menu ...)
	 (menu ...))
