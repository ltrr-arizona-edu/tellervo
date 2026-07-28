# Tellervo documentation

The canonical LaTeX manual source is in `Documentation/Manual`. The other files
in `Documentation` are supporting technical notes, diagrams, and legacy export
formats; they are not a second manual source tree.

## Build the manual

The build requires a TeX distribution containing `latexmk`, `pdflatex`,
`bibtex`, and `makeindex`.

```bash
make -C Documentation/Manual
```

This runs the complete bibliography, index, and cross-reference build. The
working PDF is `Documentation/Manual/tellervo-manual.pdf`.

To update the PDF distributed with the project:

```bash
make -C Documentation/Manual publish
```

This copies the completed PDF to `Documentation/tellervo-manual.pdf`.

To remove generated LaTeX files:

```bash
make -C Documentation/Manual clean
```

Generated LaTeX files are ignored by Git. Source files, bibliography files,
styles, and images remain tracked.
