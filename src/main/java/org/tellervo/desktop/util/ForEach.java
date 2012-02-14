/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.util;

// this class will help you do stuff for each (get it?  get it?) sample in a folder

// ugh, disgusting.  java is a horrible language for this.  use lisp, dumbass.
// (will that involve porting tellervo to lisp?)
// -- do i need common lisp?  will scheme do?

public class ForEach {

    /*
      ; inputs:
      ; -- folder: a folder to start at
      ; -- name-filter: function (of a filename) to determine whether to load a file
      ; -- sample-filter: function (of a sample) to determine whether to use a sample
      ; -- x-function: function (of a sample) to generate x-coordinate
      ; -- y-function: function (of a sample) to generate y-coordinate
      ; output:
      ; -- a file of x,y data points
      (defun make-graph-data (folder name-filter sample-filter x-function y-function)
         (let ((files (directory (concatenate 'string folder "/*") :files t :directories nil))
	    (remove-if-not name-filter files) ; apply filename filter
	    (setq files (mapcar 'load-sample files)) ; load files
	    (remove-if-not sample-filter files) ; apply sample filter
	    (loop for s in files do
	       (format t "~D ~D~%" (funcall x-function s) (funcall y-function s))) ; compute and print x,y

	    ; now do subdirs
	    (loop for folder in (directory (concatenate 'string folder "/*") :files nil :directories t)
	       do (make-graph-data folder name-filter sample-filter x-function y-function))

; todo: make filters optional

     ; use the above to get mean sensitivity versus latitude graph for raw samples
     (make-graph-data "g:\data"
                      #'(lambda (f) t)
		      #'(lambda (s) (sample-raw-p s))
		      #'(lambda (s) (mean-sensitivity s))
		      #'(lambda (s) (site-latitude (sample-site s))))

     ; 13 lines of code ... that's probably much better than java could ever do
     ; (but i don't have load-sample, mean-sensitivity, etc.
     */

    /*
      ;; here's how CLOS methods work:
      (defclass sample ()
         ((title :accessor sample-title :type string :initform "Untitled")))
         ; actually, it should be meta (right?), initform'd by cloning a +const+ alist
      (defmethod mean-sensitivity ((s sample))
         0) ; i have this code sitting around somewhere...
      (setq x (make-instance 'sample))
      (mean-sensitivity x)
     */
}
