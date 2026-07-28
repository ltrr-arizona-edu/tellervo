MANUAL := tellervo-manual
PUBLISHED_PDF := ../tellervo-manual.pdf

.PHONY: all publish clean

all: $(MANUAL).pdf

$(MANUAL).pdf: $(wildcard *.tex) $(wildcard *.bib) manual.cls computer-manual.sty
	latexmk -pdf -interaction=nonstopmode -halt-on-error $(MANUAL).tex

publish: $(MANUAL).pdf
	cp $(MANUAL).pdf $(PUBLISHED_PDF)

clean:
	latexmk -C $(MANUAL).tex
	rm -f *.aux *.bbl *.blg *.idx *.ilg *.ind *.lof *.lot *.out *.toc
