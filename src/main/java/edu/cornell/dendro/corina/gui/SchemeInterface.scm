;;
;; SPACING
;;

; call like (strut 'vertical 8)
(define (strut type size)
  (invoke-static <javax.swing.Box>
		 (if (eq? type 'vertical) 'createVerticalStrut 'createHorizontalStrut)
		 size))

; call like (empty-border 14 20 20 20)
(define (empty-border top left bottom right)
  (invoke-static <javax.swing.BorderFactory>
                 'createEmptyBorder top left bottom right))

;;
;; CONTROLS
;;

; call like (label "hello, world")
(define (label text)
  ((primitive-constructor <javax.swing.JLabel> (<String>)) text))

; call like (separator 'horizontal)
(define (separator type)
  ((primitive-constructor <corina.util.JLine> (<int>))
   (cond ((eq? type 'horizontal) (static-field <corina.util.JLine> 'HORIZONTAL))
	 ((eq? type 'vertical) (static-field <corina.util.JLine> 'VERTICAL))
	 (t (static-field <corina.util.JLine> 'HORIZONTAL)))))

(define (textfield text columns)
  ((primitive-constructor <javax.swing.JTextField> (<String> <int>)) text columns))

; WRITE ME: support for default buttons
(define (button text)
  ((primitive-constructor <javax.swing.JButton> (<String>)) text))

;;
;; LAYOUTS
;;

; add each element of |items| to |panel|; used by *-layout
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

; call like (box-layout 'vertical)
(define (box-layout orientation) . components)
  ((primitive-constructor <javax.swing.BoxLayout> (<java.awt.Container> <int>)) p
	     (cond ((eq? orientation 'horizontal) 0)
		   ((eq? orientation 'vertical) 1)
		   (t 0)))))
    (invoke p 'setLayout f)
    (add-to-layout p components)
    p))

(define (grid-layout rows columns)
  ((primitive-constructor <java.awt.GridLayout> (<int> <int>)) rows columns))

;;
;; CONTAINERS
;;

; call like (frame title: "My First Window" guts) -- user still has to pack/show
(define (frame contents)
  (let* ((f ((primitive-constructor <javax.swing.JFrame> (<String>)) text))
	 (p (invoke f 'getContentPane)))
    (invoke p 'add contents)
    f))

(define (dialog contents)
  ())

(define (panel blah)
  ())

; scrollpane -- make something scrollable
(define (scrollpane target)
   ((primitive-constructor <javax.swing.JScrollPane> (<javax.swing.JComponent>)) target))
