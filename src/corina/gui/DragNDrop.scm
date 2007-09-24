;
; convert the symbol 'copy to the constant DnDConstants.ACTION_COPY
;
(define (dnd-make-action action)
   (static-field <java.awt.dnd.DnDConstants>
      ; SCHEME CASE STATEMENT?
      (cond
         ((eq? action 'copy) 'ACTION_COPY)
         ((eq? action 'copy-or-move) 'ACTION_COPY_OR_MOVE)
         ((eq? action 'link) 'ACTION_LINK)
         ((eq? action 'move) 'ACTION_MOVE)
         ((eq? action 'none) 'ACTION_NONE)
         ((eq? action 'reference) 'ACTION_REFERENCE) ;=link
         (else 'ACTION_NONE)))) ; somebody screwed up, do nothing

;
; convert user datatype ('string, 'filelist) to system datatype
;
(define (dnd-make-datatype type)
   (static-field <java.awt.datatransfer.DataFlavor>
   ; CASE STMT?
      (cond
         ((eq? type 'string) 'stringFlavor)
         ((eq? type 'filelist) 'javaFileListFlavor)
         (t 'stringFlavor)))) ; everything's more-or-less a string, no?

;
; make an anonymous drop-listener
;
(define (dnd-make-drop-listener action datatype callback)
   (object (<java.awt.dnd.DropTargetListener>)
      ((dragEnter (e :: <java.awt.dnd.DropTargetDragEvent>)) :: <void>
         (invoke e 'acceptDrag (dnd-make-action action)))
      ((dragExit (e :: <java.awt.dnd.DropTargetEvent>)) :: <void>
         nil)
      ((dragOver (e :: <java.awt.dnd.DropTargetDragEvent>)) :: <void>
         nil)
      ((drop (e :: <java.awt.dnd.DropTargetDropEvent>)) :: <void>
         (let ((t (invoke e 'getTransferable))
                 (d (dnd-make-datatype datatype)))
            (if (invoke t 'isDataFlavorSupported d)
               (begin
                  (invoke e 'acceptDrop (dnd-make-action action))
                  (callback (invoke t 'getTransferData d))
                  (invoke (invoke e 'getDropTargetContext) 'dropComplete 'true))
               (invoke e 'rejectDrop))))

; call (invoke e 'rejectDrop) on any exception?  (how?)

      ((dropActionChanged (e :: <java.awt.dnd.DropTargetDragEvent>)) :: <void>
         nil)))

;;
;; PUBLIC: allow ACTION drops onto COMPONENT of type DATATYPE, by using function CALLBACK
;;
(define (accept-drops component action datatype callback)
   ((primitive-constructor <java.awt.dnd.DropTarget> (<java.awt.Component> <java.awt.dnd.DropTargetListener>))
     component (dnd-make-drop-listener action datatype callback)))

;;
;; PUBLIC: construct a draggable filelist
;;
;(define (make-transferable-filelist filename)
;   (object (<java.awt.datatransfer.Transferable>)
;      ((getTransferData (f :: <java.awt.datatransfer.DataFlavor>) :: <java.lang.Object>)
;         (if (not (invoke f 'equals <java.awt.datatransfer.DataFlavor> 'javaFileListFlavor))
;            (throw-primitive <java.awt.datatransfer.UnsupportedFlavorException>) ; TODO: of flavor?
;           (invoke-static <java.util.Collections> 'singletonList (make <java.io.File> filename))))
;      ((getTransferDataFlavors () :: <???>) ; TODO: sig? (DataFlavor[])
;         (???)) ; TODO: return new DataFlavor[] {DataFlavor.javaFileListFlavor};
;      ((isDataFlavorSupported (f :: <java.awt.datatransfer.DataFlavor>) :: <boolean>)
;         (invoke f 'equals (static-field <java.awt.datatransfer.DataFlavor> 'javaFileListFlavor)))))
;-- this is waay too icky to implement in kawa.  it might not even be possible.  push up to java.

;;
;; PUBLIC: construct a draggable string
;;
(define (make-transferable-string string)
   ((primitive-constructor <java.awt.datatransfer.StringSelection> (<String>)) string))

;;
;; common drag cursors
;;
(define (dnd-make-cursor action disallow)
   (static-field <java.awt.dnd.DragSource>
      ; SCHEME CASE STATEMENT?
      (cond
         ((eq? action 'copy) (if disallow 'DefaultCopyNoDrop 'DefaultCopyDrop))
         ((eq? action 'link) (if disallow 'DefaultLinkNoDrop 'DefaultLinkDrop))
         ((eq? action 'reference) (if disallow 'DefaultLinkNoDrop 'DefaultLinkDrop)) ; =link
         ((eq? action 'move) (if disallow 'DefaultMoveNoDrop 'DefaultMoveDrop))
         ((eq? action 'copy-or-move) (if disallow 'DefaultMoveNoDrop 'DefaultMoveDrop)) ; use 'move icon
         (else 'DefaultCopyNoDrop)))) ; what to do?  somebody screwed up, and there's no noop possible here

;
; make an anonymous draglistener
;
(define (dnd-make-drag-listener cursor callback)
   (let ((l (object (<java.awt.dnd.DragGestureListener> <java.awt.dnd.DragSourceListener>)
      ((dragGestureRecognized (e :: <java.awt.dnd.DragGestureEvent>)) :: <void>
         (invoke e 'startDrag cursor (callback) (this)))

      ; if you start to drag, you need to be able to handle these, too.  but i don't care about them.
      ((dragDropEnd (e :: <java.awt.dnd.DragSourceDropEvent>)) :: <void>
         nil)
      ((dragEnter (e :: <java.awt.dnd.DragSourceDragEvent>)) :: <void>
         nil)
      ((dragExit (e :: <java.awt.dnd.DragSourceEvent>)) :: <void>
         nil)
      ((dragOver (e :: <java.awt.dnd.DragSourceDragEvent>)) :: <void>
         nil)
      ((dropActionChanged (e :: <java.awt.dnd.DragSourceDragEvent>)) :: <void>
         nil))))
   l))

;;
;; PUBLIC: allow dragging from COMPONENT using ACTION; CALLBACK should create a transferable (see above)
;;
(define (accept-drags component action callback)
   (let ((s ((primitive-constructor <java.awt.dnd.DragSource> ()))))
      (invoke s 'createDefaultDragGestureRecognizer component (dnd-make-action action) (dnd-make-drag-listener (dnd-make-cursor action #t) callback))))
