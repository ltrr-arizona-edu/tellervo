;;
;; drag-n-drop
;;
(let ((a (label "Label A")))
   (frame "Drag-n-Drop Test" a)
   (accept-drops a 'copy 'string
      (lambda (o) (invoke a 'setText o)))
   (accept-drops a 'copy 'filelist
      (lambda (o) (invoke a 'setText (string-append "files: " o)))))

;; want: 'choice is (list/radiobuttons/checkboxes) -- list auto-scrolls
; (choice 'select-one 'buttons '("A" "B" "C"))
; (choice 'select-any 'buttons '("A" "B" "C"))
; (choice 'select-one 'list '("A" "B" "C"))
; (choice 'select-one 'list '("A" "B" "C") on-select: (lambda (o) (...)))
; on-click, on-press, on-release?  allow all of mouseevent, mousemotionevent's methods.
