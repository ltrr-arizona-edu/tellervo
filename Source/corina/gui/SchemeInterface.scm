; (strut 'vertical 8)
(define (strut type size)
  (invoke-static <javax.swing.Box>
		 (if (eq? type 'vertical) 'createVerticalStrut 'createHorizontalStrut)
		 size))

; (label "hello, world")
(define (label text)
  ((primitive-constructor <javax.swing.JLabel> (<String>)) text))

; call like (separator 'horizontal)
(define (separator type)
  ((primitive-constructor <javax.swing.JSeparator> (<int>))
   (cond ((eq? type 'horizontal) 0)
	 ((eq? type 'vertical) 1)
	 (t 0))))

;; add each element of |items| to |panel|; used by *-layout
(define (add-to-layout panel items)
  (if (not (eq? items ()))
      (begin
	(invoke panel "add" (car items))
	(add-to-layout panel (cdr items)))))

(define (border-layout north west center east south)
  (let ((p ((primitive-constructor <javax.swing.JPanel> ())))
	(b ((primitive-constructor <java.awt.BorderLayout> ()))))
    (invoke p 'setLayout b)
    (if (not (eq? () north))
	(invoke p 'add north (static-field <java.awt.BorderLayout> 'NORTH)))
    (if (not (eq? () south))
	(invoke p 'add south (static-field <java.awt.BorderLayout> 'SOUTH)))
    (if (not (eq? () east))
	(invoke p 'add east (static-field <java.awt.BorderLayout> 'EAST)))
    (if (not (eq? () west))
	(invoke p 'add west (static-field <java.awt.BorderLayout> 'WEST)))
    (if (not (eq? () center))
	(invoke p 'add center (static-field <java.awt.BorderLayout> 'CENTER)))
    p))

(define (border-layout-add panel components)
  (if (> 0 (length components))
      (let ((where (caar components))
	    (comp (cadr components)))
	(cond ((eq where 'north)
	       (invoke panel 'add comp (static-field <java.awt.BorderLayout> 'NORTH)))
	      ((eq where 'south)
	       (invoke panel 'add comp (static-field <java.awt.BorderLayout> 'SOUTH)))
	      ((eq where 'east)
	       (invoke panel 'add comp (static-field <java.awt.BorderLayout> 'EAST)))
	      ((eq where 'west)
	       (invoke panel 'add comp (static-field <java.awt.BorderLayout> 'WEST)))
	      ((eq where 'center)
	       (invoke panel 'add comp (static-field <java.awt.BorderLayout> 'CENTER))))
	(border-layout-add layout (cdr components)))))

; call like (border-layout (center x) (south y))
(define (border-layout . components)
  (let ((panel ((primitive-constructor <javax.swing.JPanel> ())))
	(layout ((primitive-constructor <java.awt.BorderLayout> ()))))
    (invoke panel 'setLayout layout)
    (border-layout-add panel components)
    panel))

(define (flow-layout orientation . components)
  (let ((p ((primitive-constructor <javax.swing.JPanel> ())))
	(f ((primitive-constructor <java.awt.FlowLayout> (<int>))
	    (cond ((eq? orientation 'left-to-right) 0)
		  ((eq? orientation 'centered) 1)
		  ((eq? orientation 'right-to-left) 2)
		  (t 0)))))
    (invoke p 'setLayout f)
    (add-to-layout p components)
    p))

; call like (box-layout 'vertical a b c)
(define (box-layout orientation . components)
  (let* ((p ((primitive-constructor <javax.swing.JPanel> ())))
	 (f ((primitive-constructor <javax.swing.BoxLayout> (<java.awt.Container> <int>)) p
	     (cond ((eq? orientation 'horizontal) 0)
		   ((eq? orientation 'vertical) 1)
		   (t 0)))))
    (invoke p 'setLayout f)
    (add-to-layout p components)
    p))

; call like (frame "My First Window" guts)
(define (frame text contents)
  (let* ((f ((primitive-constructor <javax.swing.JFrame> (<String>)) text))
	 (p (invoke f 'getContentPane)))
    (invoke p 'add contents)
    (invoke f 'pack)
    (invoke f 'show)
    f))

(define (textfield text columns)
  ((primitive-constructor <javax.swing.JTextField> (<String> <int>)) text columns))

; WRITE ME: support for default buttons
(define (button text)
  ((primitive-constructor <javax.swing.JButton> (<String>)) text))

(define (grid-layout rows columns . components)
  (let ((p ((primitive-constructor <javax.swing.JPanel> ())))
	(g ((primitive-constructor <java.awt.GridLayout> (<int> <int>)) rows columns)))
    (invoke p 'setLayout g)
    (add-to-layout p components)
    p))

; lists -- oops, "list" is already taken
;(define (jlist items))
; HOW TO IMPLEMENT:
; -- 2 lists, scrollable, initialized with certain elements
; -- drag-n-drop between them
; -- once that's done, it'll be ready to handle anything
   
; scrollpanel
(define (scrollpane target)
   ((primitive-constructor <javax.swing.JScrollPane> (<javax.swing.JComponent>)) target))

;;
;; EVENT HANDLING
;;

; add an action to a button -- hmm, not pretty.  how about the ability to add in-line? (button "Hello, world." action: (lambda () (write "Hi!")))
(define (add-click-action button action)
  (invoke button 'addActionListener
	  (object (<javax.swing.AbstractAction>)
		  ((actionPerformed (event :: <java.awt.event.ActionEvent>)) :: <void>
		   (action)))))
