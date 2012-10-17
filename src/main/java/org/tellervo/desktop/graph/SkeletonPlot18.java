package org.tellervo.desktop.graph;

import java.awt.*;

public class SkeletonPlot18 extends java.applet.Applet {
	public SkeletonPlot18() {
	}

    Image offscreenImg; //setup offscreen area
    Graphics offscreenG;
    Label sensitivityLabel, numberOfRingsLabel, absoluteValueLabel, firstDifferenceLabel;
    Font fb = new Font("TimesRoman", Font.BOLD, 16);
    Font fbsmall = new Font("TimesRoman", Font.BOLD, 12);
    Font fbi = new Font("TimesRoman", Font.ITALIC + Font.BOLD, 16);
    Font fbismall = new Font("TimesRoman", Font.ITALIC + Font.BOLD, 12);
    int numberOfRings = 61;
    int masterLengthFactor = 6;
    double[] index = new double[numberOfRings*masterLengthFactor];
    double[] skeletonNormalBoneX = new double[numberOfRings];
    double[] skeletonNormalBoneY = new double[numberOfRings];
    double[] skeletonMissingBoneX = new double[numberOfRings];
    double[] skeletonFalseBoneX = new double[numberOfRings];
    double[] skeletonWideBoneX = new double[numberOfRings];
    double[] masterBoneX = new double[numberOfRings*masterLengthFactor];
    double[] masterBoneY = new double[numberOfRings*masterLengthFactor];
    double[] masterWideBoneX = new double[numberOfRings*masterLengthFactor];
    int[] latewoodColor = new int[numberOfRings+10];
    double latewoodRandom;
    double masterBoneNormalCutoff = .4;
    double masterBoneWideCutoff = .95;
    double coreStartPosition = 0;
    double minimumIndex;
    double maximumIndex;
    int absentRingsHint;
    int numberOfFalseRingsHint;
    int absoluteValueCutoff = 2;
    int firstDifferenceCutoff = 2;
    int numberOfReplicates = 1;
    int falseRing = -1;
    int targetRingWidth = 50;
    int coreLength = 0;
    int oldX = -1000000;  //set to an unlikely value
    int numberOfSkeletonNormalBones = 0;
    int numberOfSkeletonMissingBones = 0;
    int numberOfSkeletonFalseBones = 0;
    int numberOfSkeletonWideBones = 0;
    int numberOfMasterBones = 0;
    int numberOfMasterWideBones = 0;
    int coreMountLeftX = 10;
    int coreMagLevel = 1;
    int skeletonLeftX = 10;
    int masterLeftX = 10;
    int numberOfMasterRings = numberOfRings*masterLengthFactor;
    double graphScale = 2;
    int lineSpacing = 4;
    double masterStartYearSeed = 0;
    //for esthetics only, start/end grapghs on 5th year of decade
    int masterStartYear = 0;
    int numberOfMasterLines = 0;
    int numberOfSkeletonLines = 0;
    final int coreMountUpperY = 136;
    final int coreMountEdge = 10;
    final int coreHeight = 20;
    final int coreMountHeight = coreMountEdge*2+coreHeight;
    int graphEmptySpace =  (int)Math.round(5*lineSpacing*graphScale); //scale empty space on left
    int skeletonHeight  = (int)Math.round(15*lineSpacing*graphScale); //scale height of skeleton graph
    int masterHeight    = (int)Math.round(30*lineSpacing*graphScale); //scale height of master graph
    int skeletonUpperY = coreMountUpperY+coreMountHeight+16;
    int masterUpperY = skeletonUpperY + skeletonHeight + 1;
    int answersLeftX = 10;
    int answersUpperY = coreMountUpperY-8;
    int helpLeftX = 10;
    int helpUpperY = skeletonUpperY + 50;
    Scrollbar changeSensitivity, changeAbsoluteValue, changeFirstDifference, changeNumberOfRings;
    Button restartApplet, redoMasterMarks, hintShow, answersShow, masterShow, helpShow;
    Checkbox missingsAllow, falsesAllow,
             oneBy, twoBy, threeBy,
             largeGraphs, smallGraphs, tinyGraphs,
             mark, erase,
             markNormal, markMissing, markFalse, markWide;
    CheckboxGroup coreMag = new CheckboxGroup();
    CheckboxGroup graphMag = new CheckboxGroup();
    CheckboxGroup drawErase = new CheckboxGroup();
    CheckboxGroup markType = new CheckboxGroup();
    int markTypeInt = 1;//1 = normal, 2 = missing, 3 = false, 4 = wide
    boolean redrawCore = true;
    boolean redrawSkeletonPlot = true;
    boolean redrawMasterPlot = true;
    boolean showMaster = false;
    boolean draw = true;
    boolean normalMark = true;
    boolean missingMark = false;
    boolean falseMark = false;
    boolean showHelp = false;
    boolean showAnswers = false;
    boolean showHint = false;
    boolean allowMissingRings = false;
    boolean allowFalseRings = false;
    String[] answers = new String[5];
    Color myPanel          = new Color(204,255,255); //15,1
    Color myPink           = new Color(204,204,255); //15,1
    Color myLightGreen     = new Color(102,255,102); //10,10
    Color myDarkGreen      = new Color(000,204,051); //11,9
    Color myBeige          = new Color(255,204,153); //12,13
    Color myCoreMount      = new Color(204,255,255); //9,8
    Color myEarlywood      = new Color(255,255,051); //12,12
    Color myLightLatewood  = new Color(255,204,051); //2,13
    Color myMediumLatewood = new Color(204,153,000); //1,13
    Color myDarkLatewood   = new Color(153,102,000); //3,13
    Frame myFrame;
    Frame helpWindow;
    Panel p2;

    void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh, int wx, int wy) {
        gbc.gridx = gx;
        gbc.gridy = gy;
        gbc.gridwidth = gw;
        gbc.gridheight = gh;
        gbc.weightx = wx;
        gbc.weighty = wy;
    }

    public void init() {
        helpWindow = new Frame();
        helpWindow.resize(150,150);
        helpWindow.hide();
        Container c = this;
        while(!(c instanceof Frame)) c = c.getParent();
        myFrame =  (Frame)c;
        //initiate offscreen area
        offscreenImg = createImage(this.size().width, this.size().height);
        offscreenG = offscreenImg.getGraphics();
        //gridbaglayout
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        Panel p = new Panel();
       
        p.setLayout(gridbag);
//        p.setBackground(Color.cyan);
        //create checkbox for missing and false rings
        buildConstraints(constraints, 0, 0, 1, 1, 0, 0);
        constraints.fill = GridBagConstraints.BOTH;
        Label possibleLabel = new Label("Possible", Label.CENTER);
        gridbag.setConstraints(possibleLabel, constraints);
        p.add(possibleLabel);
        //
        buildConstraints(constraints, 0, 1, 1, 1, 0, 0);
        Label ringGrowthLabel = new Label("Ring Growth", Label.CENTER);
        gridbag.setConstraints(ringGrowthLabel, constraints);
        p.add(ringGrowthLabel);
        //
        buildConstraints(constraints, 0, 2, 1, 1, 0, 0);
        Label anomalyLabel = new Label("Anomalies", Label.CENTER);
        gridbag.setConstraints(anomalyLabel, constraints);
        p.add(anomalyLabel);
        //
        buildConstraints(constraints, 0, 3, 1, 1, 0, 0);
        missingsAllow = new Checkbox("Absents", null, false);
        gridbag.setConstraints(missingsAllow, constraints);
        p.add(missingsAllow);
        //
        buildConstraints(constraints, 0, 4, 1, 1, 0, 0);
        falsesAllow = new Checkbox("Falses", null, false);
        gridbag.setConstraints(falsesAllow, constraints);
        p.add(falsesAllow);
        //
        buildConstraints(constraints, 1, 0, 2, 1, 0, 0);
        Label magnificationLabel = new Label("Magnification", Label.CENTER);
        gridbag.setConstraints(magnificationLabel, constraints);
        p.add(magnificationLabel);
        //
        buildConstraints(constraints, 1, 1, 1, 1, 0, 0);
        Label coreLabel = new Label("Core", Label.CENTER);
        gridbag.setConstraints(coreLabel, constraints);
        p.add(coreLabel);
        //
        buildConstraints(constraints, 2, 1, 1, 1, 0, 0);
        Label graphsLabel = new Label("Graphs", Label.CENTER);
        gridbag.setConstraints(graphsLabel, constraints);
        p.add(graphsLabel);
        //
        buildConstraints(constraints, 1, 2, 1, 1, 0, 0);
        oneBy = new Checkbox("1x", coreMag, true);
        gridbag.setConstraints(oneBy, constraints);
        p.add(oneBy);
        //
        buildConstraints(constraints, 1, 3, 1, 1, 0, 0);
        twoBy = new Checkbox("2x", coreMag, false);
        gridbag.setConstraints(twoBy, constraints);
        p.add(twoBy);
        //
        buildConstraints(constraints, 1, 4, 1, 1, 0, 0);
        threeBy = new Checkbox("3x", coreMag, false);
        gridbag.setConstraints(threeBy, constraints);
        p.add(threeBy);
        //
        buildConstraints(constraints, 2, 2, 1, 1, 0, 0);
        largeGraphs = new Checkbox("Large", graphMag, true);
        gridbag.setConstraints(largeGraphs, constraints);
        p.add(largeGraphs);
        //
        buildConstraints(constraints, 2, 3, 1, 1, 0, 0);
        smallGraphs = new Checkbox("Medium", graphMag, false);
        gridbag.setConstraints(smallGraphs, constraints);
        p.add(smallGraphs);
        //
        buildConstraints(constraints, 2, 4, 1, 1, 0, 0);
        tinyGraphs = new Checkbox("Small", graphMag, false);
        gridbag.setConstraints(tinyGraphs, constraints);
        p.add(tinyGraphs);
        //
        buildConstraints(constraints, 3, 0, 2, 1, 0, 0);
        Label mouseActionsLabel = new Label("Mouse Actions", Label.CENTER);
        gridbag.setConstraints(mouseActionsLabel, constraints);
        p.add(mouseActionsLabel);
        //
        buildConstraints(constraints, 3, 1, 1, 2, 0, 0);
        mark = new Checkbox("Draw", drawErase, true);
        gridbag.setConstraints(mark, constraints);
        p.add(mark);
        //
        buildConstraints(constraints, 3, 3, 1, 2, 0, 0);
        erase = new Checkbox("Erase", drawErase, false);
        gridbag.setConstraints(erase, constraints);
        p.add(erase);
        //
        buildConstraints(constraints, 4, 1, 1, 1, 0, 0);
        markNormal = new Checkbox("Normal", markType, true);
        gridbag.setConstraints(markNormal, constraints);
        p.add(markNormal);
        //
        buildConstraints(constraints, 4, 2, 1, 1, 0, 0);
        markWide = new Checkbox("Wide", markType, false);
        gridbag.setConstraints(markWide, constraints);
        p.add(markWide);
        //
        buildConstraints(constraints, 4, 3, 1, 1, 0, 0);
        markMissing = new Checkbox("Absent", markType, false);
        gridbag.setConstraints(markMissing, constraints);
        p.add(markMissing);
        //
        buildConstraints(constraints, 4, 4, 1, 1, 0, 0);
        markFalse = new Checkbox("False", markType, false);
        gridbag.setConstraints(markFalse, constraints);
        p.add(markFalse);
        //
        buildConstraints(constraints, 5, 0, 1, 1, 0, 0);
        Label showLabel = new Label("Show", Label.CENTER);
        gridbag.setConstraints(showLabel, constraints);
        p.add(showLabel);
        //
        buildConstraints(constraints, 5, 1, 1, 1, 0, 0);
        hintShow = new Button("Hint");
        gridbag.setConstraints(hintShow, constraints);
        p.add(hintShow);
        //
        buildConstraints(constraints, 5, 2, 1, 1, 0, 0);
        answersShow = new Button("Answer");
        gridbag.setConstraints(answersShow, constraints);
        p.add(answersShow);
        //
        buildConstraints(constraints, 5, 3, 1, 1, 0, 0);
        helpShow = new Button("Help");
        gridbag.setConstraints(helpShow, constraints);
        p.add(helpShow);
        //
        buildConstraints(constraints, 5, 4, 1, 1, 0, 0);
        masterShow = new Button("Master");
        gridbag.setConstraints(masterShow, constraints);
        p.add(masterShow);
        //
        buildConstraints(constraints, 6, 0, 2, 1, 0, 0);
        Label coreSeriesLabel = new Label("Core Series", Label.CENTER);
        gridbag.setConstraints(coreSeriesLabel, constraints);
        p.add(coreSeriesLabel);
        //
        buildConstraints(constraints, 6, 1, 2, 1, 0, 0);
        Label settingsLabel1 = new Label("Settings", Label.CENTER);
        gridbag.setConstraints(settingsLabel1, constraints);
        p.add(settingsLabel1);
        //
        buildConstraints(constraints, 6, 2, 1, 1, 0, 0);
        sensitivityLabel = new Label("Sensitivity: 1", Label.CENTER);
        gridbag.setConstraints(sensitivityLabel, constraints);
        p.add(sensitivityLabel);
        //
        buildConstraints(constraints, 7, 2, 1, 1, 0, 0);
        changeSensitivity = new Scrollbar(Scrollbar.HORIZONTAL,1,1,1,10);
        gridbag.setConstraints(changeSensitivity, constraints);
        p.add(changeSensitivity);
        //
        buildConstraints(constraints, 6, 3, 1, 1, 0, 0);
        numberOfRingsLabel = new Label("No. of Rings: 61", Label.CENTER);
        gridbag.setConstraints(numberOfRingsLabel, constraints);
        p.add(numberOfRingsLabel);
        //
        buildConstraints(constraints, 7, 3, 1, 1, 0, 0);
        changeNumberOfRings = new Scrollbar(Scrollbar.HORIZONTAL,61,1,11,401);
        changeNumberOfRings.setLineIncrement(10);
        gridbag.setConstraints(changeNumberOfRings, constraints);
        p.add(changeNumberOfRings);
        //
        buildConstraints(constraints, 6, 4, 2, 1, 0, 0);
        restartApplet = new Button("Restart a New Core");
        gridbag.setConstraints(restartApplet, constraints);
        p.add(restartApplet);
        //
        buildConstraints(constraints, 8, 0, 2, 1, 0, 0);
        Label masterMarksLabel = new Label("Master Marks", Label.CENTER);
        gridbag.setConstraints(masterMarksLabel, constraints);
        p.add(masterMarksLabel);
        //
        buildConstraints(constraints, 8, 1, 2, 1, 0, 0);
        Label settingsLabel2 = new Label("Settings", Label.CENTER);
        gridbag.setConstraints(settingsLabel2, constraints);
        p.add(settingsLabel2);
        //
        buildConstraints(constraints, 8, 2, 1, 1, 0, 0);
        absoluteValueLabel = new Label("Abs. Value: 2", Label.CENTER);
        gridbag.setConstraints(absoluteValueLabel, constraints);
        p.add(absoluteValueLabel);
        //
        buildConstraints(constraints, 9, 2, 1, 1, 0, 0);
        changeAbsoluteValue = new Scrollbar(Scrollbar.HORIZONTAL,2,1,1,10);
        gridbag.setConstraints(changeAbsoluteValue, constraints);
        p.add(changeAbsoluteValue);
        //
        buildConstraints(constraints, 8, 3, 1, 1, 0, 0);
        firstDifferenceLabel = new Label("First Diff.: 2", Label.CENTER);
        gridbag.setConstraints(firstDifferenceLabel, constraints);
        p.add(firstDifferenceLabel);
        //
        buildConstraints(constraints, 9, 3, 1, 1, 0, 0);
        changeFirstDifference = new Scrollbar(Scrollbar.HORIZONTAL,2,1,1,10);
        gridbag.setConstraints(changeFirstDifference, constraints);
        p.add(changeFirstDifference);
        //
        buildConstraints(constraints, 8, 4, 2, 1, 0, 0);
        redoMasterMarks = new Button("Redo Marks on Master");
        gridbag.setConstraints(redoMasterMarks, constraints);
        p.add(redoMasterMarks);
        add(p);
        p2 = new Panel();
        p2.setLayout(new BorderLayout());
        p2.add("North", p);
        add(p2);
        this.setMinimumSize(new Dimension(800,800));
        startUp();
    }

    //handle scrollbar event
    public boolean handleEvent(Event evt) {
        if (evt.target instanceof Scrollbar) {
            if (evt.target.equals(changeSensitivity)) {
                sensitivityLabel.setText("Sensitivity: "+String.valueOf(((Scrollbar)evt.target).getValue()));
                numberOfReplicates = ((Scrollbar)evt.target).getValue();
            }
            else if (evt.target.equals(changeAbsoluteValue)) {
                absoluteValueLabel.setText("Abs. Value: "+String.valueOf(((Scrollbar)evt.target).getValue()));
                absoluteValueCutoff = ((Scrollbar)evt.target).getValue();
            }
            else if (evt.target.equals(changeFirstDifference)) {
                firstDifferenceLabel.setText("First Diff.: "+String.valueOf(((Scrollbar)evt.target).getValue()));
                firstDifferenceCutoff = ((Scrollbar)evt.target).getValue();
            }
            else {
                numberOfRingsLabel.setText("No. of Rings: "+String.valueOf(((Scrollbar)evt.target).getValue()));
                numberOfRings = ((Scrollbar)evt.target).getValue();
            }
        }
        return super.handleEvent(evt);
    }

    //allow changing all choices on the applet
    public boolean action(Event evt, Object arg) {
        if (evt.target instanceof Button) {
            if (evt.target.equals(restartApplet)) {
                numberOfSkeletonNormalBones = 0;
                numberOfSkeletonMissingBones = 0;
                numberOfSkeletonFalseBones = 0;
                numberOfSkeletonWideBones = 0;
                coreMountLeftX = 10;
                skeletonLeftX = 10;
                masterLeftX = 10;
                falseRing = -1;
                redrawCore = true;
                redrawSkeletonPlot = true;
                redrawMasterPlot = true;
                numberOfMasterBones = 0;
                numberOfMasterWideBones = 0;
                index = new double[numberOfRings*masterLengthFactor];
                skeletonNormalBoneX = new double[numberOfRings];
                skeletonNormalBoneY = new double[numberOfRings];
                skeletonMissingBoneX = new double[numberOfRings];
                skeletonFalseBoneX = new double[numberOfRings];
                skeletonWideBoneX = new double[numberOfRings];
                masterBoneX = new double[numberOfRings*masterLengthFactor];
                masterBoneY = new double[numberOfRings*masterLengthFactor];
                masterWideBoneX = new double[numberOfRings*masterLengthFactor];
                latewoodColor = new int[numberOfRings+10];
                numberOfMasterRings = numberOfRings*masterLengthFactor;
                startUp();
            }
            //else redo marks on the master plot
            else if (evt.target.equals(redoMasterMarks)) {
                findMasterMarks();
                redrawMasterPlot = true;
                repaint();
            }
            else if (evt.target.equals(answersShow)) {
                showAnswers = !showAnswers;
                redrawCore = true;
                redrawSkeletonPlot = true;
                repaint();
            }
            else if (evt.target.equals(hintShow)) {
                showHint = !showHint;
                redrawCore = true;
                repaint();
            }
            else if (evt.target.equals(masterShow)) {
                showMaster = !showMaster;
                redrawMasterPlot = true;
                repaint();
            }
            else if (evt.target.equals(helpShow)) {
                if (helpWindow.isShowing()) helpWindow.hide();
                else helpWindow.show();
                showHelp = !showHelp;
                repaint();
            }
        }
        else if (evt.target instanceof Checkbox) {
            if (evt.target.equals(markNormal)) markTypeInt = 1;
            else if (evt.target.equals(markMissing)) markTypeInt = 2;
            else if (evt.target.equals(markFalse)) markTypeInt = 3;
            else if (evt.target.equals(markWide)) markTypeInt = 4;
            else if (evt.target.equals(mark)) draw = true;
            else if (evt.target.equals(erase)) draw = false;
            else if (evt.target.equals(missingsAllow)) allowMissingRings = !allowMissingRings;
            else if (evt.target.equals(falsesAllow)) allowFalseRings = !allowFalseRings;
            else if (evt.target.equals(oneBy)) {
                coreMagLevel = 1;
                redrawCore = true;
                repaint();
            }
            else if (evt.target.equals(twoBy)) {
                coreMagLevel = 2;
                redrawCore = true;
                repaint();
            }
            else if (evt.target.equals(threeBy)) {
                coreMagLevel = 3;
                redrawCore = true;
                repaint();
            }
            else if (evt.target.equals(largeGraphs) || evt.target.equals(smallGraphs) || evt.target.equals(tinyGraphs)) {
                if (evt.target.equals(largeGraphs)) graphScale = 2;
                else if (evt.target.equals(smallGraphs)) graphScale = 1.5;
                else if (evt.target.equals(tinyGraphs)) graphScale = 1;
                graphEmptySpace = (int)Math.round( 5*lineSpacing*graphScale);
                skeletonHeight  = (int)Math.round(15*lineSpacing*graphScale);
                masterHeight    = (int)Math.round(30*lineSpacing*graphScale);
                skeletonUpperY = coreMountUpperY+coreMountHeight+16;
                masterUpperY = skeletonUpperY + skeletonHeight + 1;
                redrawSkeletonPlot = true;
                redrawMasterPlot = true;
                repaint();
            }
        }
        return true;
    }

    public void startUp() {
        //initiate answer statements
        answers[0] = "All of these are part of the answer: ";
        answers[1] = "Start year: ";
        answers[2] = "Missing rings: ";
        answers[3] = "False rings: ";
        answers[4] = "End year: ";
        //to select trial core from master series, need a random start position
        //ensure that this value is between 0.05 and 0.80 so that the trial core doesn't touch end of index master
        masterStartYearSeed = Math.random();
        coreStartPosition = 0;
        masterStartYear = (int)Math.round(1998-numberOfMasterRings-masterStartYearSeed*100)-
                         ((int)Math.round(1998-numberOfMasterRings-masterStartYearSeed*100))%10 - 5;
        numberOfMasterLines = numberOfMasterRings +(10-(numberOfMasterRings+masterStartYear)%10)+5;
        numberOfSkeletonLines = numberOfRings+(10-numberOfRings%10)+5;
        coreLength = 0;
        while (coreStartPosition < 0.05 || coreStartPosition > 0.80) coreStartPosition = Math.random();
        answers[1] += masterStartYear+(int)Math.round(numberOfMasterRings*coreStartPosition)+" ";
        //initiate master series of random indices ranging from 0 to 2.0
        minimumIndex = 100;
        maximumIndex = 0;
        for (int year = 0;  year < numberOfMasterRings; year++) {
            index[year] = 0;
            for (int replicate = 0; replicate < numberOfReplicates; replicate++)
                index[year] += Math.random()*2;
            index[year] /= numberOfReplicates;
            if (index[year] < minimumIndex) minimumIndex = index[year];
            else if (index[year] > maximumIndex) maximumIndex = index[year];
        }
        findMasterMarks();
        int absentRings = 0;
        absentRingsHint = 0;
        int numberOfFalseRings = 0;
        for (int year = (int)Math.round(numberOfMasterRings*coreStartPosition);
                 year < (int)Math.round(numberOfMasterRings*coreStartPosition)+numberOfRings-numberOfFalseRings;
                 year++) {
            //incorporate missing rings, but can't be first or last ring
            while (index[year+absentRings] < 0.06) {
                if (!allowMissingRings) index[year+absentRings] = 0.06;
                else
                if (year > (int)Math.round(numberOfMasterRings*coreStartPosition) &&
                    year < (int)Math.round(numberOfMasterRings*coreStartPosition)+numberOfRings-1) {
                    answers[2] += (masterStartYear+year+absentRings)+" ";
                    absentRings++;
                    absentRingsHint++;
                }
            }
            //incorporate false rings, but can't be first or last ring
            if (allowFalseRings && index[year+absentRings] > 1.95 && year > (int)Math.round(numberOfMasterRings*coreStartPosition) &&
                year < (int)Math.round(numberOfMasterRings*coreStartPosition+numberOfRings-1) &&
                numberOfFalseRings == 0 && (year-(int)Math.round(numberOfMasterRings*coreStartPosition))%10 != 0 &&
                (year-(int)Math.round(numberOfMasterRings*coreStartPosition)+1)%10 != 0) {
                falseRing = year-(int)Math.round(numberOfMasterRings*coreStartPosition);
                index[year+absentRings] = 2.00;
                answers[3] += "Rings "+(year-(int)Math.round(numberOfMasterRings*coreStartPosition))+" & "+
                                       (year-(int)Math.round(numberOfMasterRings*coreStartPosition)+1)+" are 1 ring ";
                numberOfFalseRings++;
                numberOfFalseRingsHint++;
            }
            coreLength += (int)Math.round(index[year+absentRings]/2*targetRingWidth);
            latewoodRandom = Math.random();
            if (latewoodRandom > .85) latewoodColor[year-(int)Math.round(numberOfMasterRings*coreStartPosition)] = 3;
            else if (latewoodRandom < .15)
                latewoodColor[year-(int)Math.round(numberOfMasterRings*coreStartPosition)] = 1;
            else latewoodColor[year-(int)Math.round(numberOfMasterRings*coreStartPosition)] = 2;
        }
        answers[4] += (masterStartYear+(int)Math.round(numberOfMasterRings*coreStartPosition)+numberOfRings+
                       absentRings-numberOfFalseRings-1)+" ";
        redrawCore = true;
        repaint();
    }

    public void findMasterMarks() {
        numberOfMasterBones = 0;
        numberOfMasterWideBones = 0;
        for (int year = 0;  year < numberOfMasterRings; year++) {
        	//find normal bones of master plot
        	/*                            1.0 - minimum Index     i.e., the difference from 1, subtracted from one
			absolute value trigger: 1.0 - ----------------------,
                              cutoff (range 1 to 10)  as cutoff -> 10, trigger -> 1.0

                          minimunIndex - maximumIndex  i.e., largest first difference possible
			first difference trigger: ---------------------------,
                            cutoff (range 1 to 10)     as cutoff -> 10, trigger -> 0
        	 */
            if (year > 0 && (index[year] < (1-(1-minimumIndex)/absoluteValueCutoff) ||
                             index[year]-index[year-1] < (minimumIndex-maximumIndex)/firstDifferenceCutoff)) {
                masterBoneX[numberOfMasterBones] = (graphEmptySpace+year*lineSpacing*graphScale)/
               (lineSpacing*graphScale);
               masterBoneY[numberOfMasterBones] = 0;
/*
                          (     index[yr]        )
                          | ==================== |
absolute value mark: 10 - |     1-minimumIndex   | * 10
                          | 1 - ---------------- |
                          (     cutoff (2 to 10) )
as Index -> 0, mark -> 10; as Index -> trigger, mark -> 0

                            (  (Index[yr] - Index[yr-1]) - (minimumIndex - maximumIndex)  )
                            | =========================================================== |
first difference mark: 10 - | minimumIndex - maximumIndex                                 | * 10
                            | --------------------------- - (minimumIndex - maximumIndex) |
                            (      cutoff (2 to 10)                                       )
as first difference -> maximum range, mark -> 10, as first difference -> trigger, mark -> 0

in either case, cutoffs = 1 cause a zero in the denominator, which is not allowed
take maximum mark of the two
note: it's possible to have a positive first difference evaluated, so set a conditional against that possibility
*/
               if (absoluteValueCutoff > 1)
                   masterBoneY[numberOfMasterBones] = 10 - index[year]/(1-(1-minimumIndex)/absoluteValueCutoff)*10;
               if (firstDifferenceCutoff > 1 && index[year] < index[year-1])
                   masterBoneY[numberOfMasterBones] = Math.max(masterBoneY[numberOfMasterBones],
                   10 - ((index[year]-index[year-1])-(minimumIndex-maximumIndex))/
                   ((minimumIndex-maximumIndex)/firstDifferenceCutoff-(minimumIndex-maximumIndex))*10);
               numberOfMasterBones++;
            }
            //find wide bones of master plot
            if (index[year] > masterBoneWideCutoff*maximumIndex) {
                masterWideBoneX[numberOfMasterWideBones] = (graphEmptySpace+year*lineSpacing*graphScale)/
                                                   (lineSpacing*graphScale);
                numberOfMasterWideBones++;
            }
        }
    }

    public boolean mouseMove(Event evt, int x, int y) {
        //if over core series
        if (x>=(int)Math.round(this.size().width/2-(this.size().width/2-coreMountLeftX)*coreMagLevel) &&
            x <(int)Math.round(this.size().width/2-(this.size().width/2-coreMountLeftX)*coreMagLevel)+coreLength*
            coreMagLevel && y>=coreMountUpperY && y<coreMountUpperY+coreMountHeight) {
            myFrame.setCursor(Frame.TEXT_CURSOR);
            showStatus("Move core series left or right");
        }
        //else if over top third of skeleton plot
        else if (x>=Math.max((int)Math.round(this.size().width/2-(this.size().width/2-skeletonLeftX)*graphScale/2),0) &&
            x< Math.min(this.size().width,(int)Math.round(this.size().width/2-(this.size().width/2-skeletonLeftX)*
                        graphScale/2+graphEmptySpace+numberOfSkeletonLines*lineSpacing*graphScale)) &&
            y>=skeletonUpperY && y<skeletonUpperY+(int)Math.round(skeletonHeight/3)) {
            myFrame.setCursor(Frame.E_RESIZE_CURSOR);
            showStatus("Move skeleton plot left or right");
        }
        //else if over middle third of master plot
        else if (x>=Math.max((int)Math.round(this.size().width/2-(this.size().width/2-masterLeftX)*graphScale/2),0) &&
            x< Math.min(this.size().width,(int)Math.round(this.size().width/2-(this.size().width/2-masterLeftX)*
                        graphScale/2+graphEmptySpace+numberOfMasterLines*lineSpacing*graphScale)) &&
            y>=masterUpperY + (int)Math.round(masterHeight*2/6) && y<masterUpperY + (int)Math.round(masterHeight*4/6) &&
            showMaster) {
            myFrame.setCursor(Frame.E_RESIZE_CURSOR);
            showStatus("Move skeleton AND master plots left or right");
        }
        //else if over bottom third of master plot
        else if (x>=Math.max((int)Math.round(this.size().width/2-(this.size().width/2-masterLeftX)*graphScale/2),0) &&
            x< Math.min(this.size().width,(int)Math.round(this.size().width/2-(this.size().width/2-masterLeftX)*
                        graphScale/2+graphEmptySpace+numberOfMasterLines*lineSpacing*graphScale)) &&
            y>=masterUpperY + (int)Math.round(masterHeight*4/6) && y<masterUpperY + masterHeight &&
            showMaster) {
            myFrame.setCursor(Frame.E_RESIZE_CURSOR);
            showStatus("Move master plot left or right");
        }
        //else if marking or erasing in skeleton plot
        else if (x>=(int)Math.round(this.size().width/2-(this.size().width/2-skeletonLeftX)*graphScale/2+graphEmptySpace) &&
            x<=(int)Math.round(this.size().width/2-(this.size().width/2-skeletonLeftX)*graphScale/2+graphEmptySpace+
            numberOfSkeletonLines*lineSpacing*graphScale) &&
            y>=skeletonUpperY+(int)Math.round(skeletonHeight/3) && y<=skeletonUpperY+skeletonHeight) {
            //if drawing
            if (draw) {
                myFrame.setCursor(Frame.HAND_CURSOR);
                if (markTypeInt == 1) showStatus("Draw normal marks on skeleton plot");
                else if (markTypeInt == 2) showStatus("Draw missing ring marks on skeleton plot");
                else if (markTypeInt == 3) showStatus("Draw false ring marks on skeleton plot");
                else showStatus("Draw wide ring marks on skeleton plot");
            }
            //else erasing
            else {
                myFrame.setCursor(Frame.CROSSHAIR_CURSOR);
                showStatus("Erase any mark type on skeleton plot");
            }
        }
        //else if marking or erasing in master plot
        else if (x>=(int)Math.round(this.size().width/2-(this.size().width/2-masterLeftX)*graphScale/2+graphEmptySpace) &&
            x<=(int)Math.round(this.size().width/2-(this.size().width/2-masterLeftX)*graphScale/2+graphEmptySpace+
            numberOfMasterLines*lineSpacing*graphScale) &&
            y>=masterUpperY && y<=masterUpperY+(int)Math.round(masterHeight*2/6) && showMaster) {
            //if drawing
            if (draw) {
                myFrame.setCursor(Frame.HAND_CURSOR);
                if (markTypeInt == 1) showStatus("Draw normal marks on master plot");
                else if (markTypeInt == 2) showStatus("You may not draw missing ring marks on master plot");
                else if (markTypeInt == 3) showStatus("You may not draw false ring marks on master plot");
                else showStatus("Draw wide ring marks on master plot");
            }
            //else erasing
            else {
                myFrame.setCursor(Frame.CROSSHAIR_CURSOR);
                showStatus("Erase any mark type on master plot");
            }
        }
        //else default
        else {
            myFrame.setCursor(Frame.DEFAULT_CURSOR);
            showStatus("");
        }
        return true;
    }

    public boolean mouseDrag(Event evt, int x, int y) {
        //remember where mouse drag starts each time
        if (oldX == -1000000)
            oldX = x;

        //if within core, move core
        if (x>=(int)Math.round(this.size().width/2-(this.size().width/2-coreMountLeftX)*coreMagLevel) &&
            x <(int)Math.round(this.size().width/2-(this.size().width/2-coreMountLeftX)*coreMagLevel)+coreLength*
                coreMagLevel && y>=coreMountUpperY && y<coreMountUpperY+coreMountHeight) {
            coreMountLeftX += (int)Math.round((x-oldX)/coreMagLevel);
            oldX = x;
            redrawCore = true;
        }
        //else if within top third of skeleton plot, move skeleton plot
        else if (x>=Math.max((int)Math.round(this.size().width/2-(this.size().width/2-skeletonLeftX)*graphScale/2),0) &&
            x< Math.min(this.size().width,(int)Math.round(this.size().width/2-(this.size().width/2-skeletonLeftX)*
                        graphScale/2+graphEmptySpace+numberOfSkeletonLines*lineSpacing*graphScale)) &&
            y>=skeletonUpperY && y<skeletonUpperY+(int)Math.round(skeletonHeight/3)) {
            skeletonLeftX += (x-oldX);
            oldX = x;
            redrawSkeletonPlot = true;
        }
        //else if within bottom third of master plot, move master plot
        else if (x>=Math.max((int)Math.round(this.size().width/2-(this.size().width/2-masterLeftX)*graphScale/2),0) &&
            x< Math.min(this.size().width,(int)Math.round(this.size().width/2-(this.size().width/2-masterLeftX)*
                        graphScale/2+graphEmptySpace+numberOfMasterLines*lineSpacing*graphScale)) &&
            y>=masterUpperY + (int)Math.round(masterHeight*4/6) && y<masterUpperY+masterHeight) {
            masterLeftX += (x-oldX);
            redrawMasterPlot = true;
            oldX = x;
        }
        //else if within middle third of master plot, move both plots
        else if (x>=Math.max((int)Math.round(this.size().width/2-(this.size().width/2-masterLeftX)*graphScale/2),0) &&
            x< Math.min(this.size().width,(int)Math.round(this.size().width/2-(this.size().width/2-masterLeftX)*
                        graphScale/2+graphEmptySpace+numberOfMasterLines*lineSpacing*graphScale)) &&
            y>=masterUpperY + (int)Math.round(masterHeight*2/6) && y<masterUpperY + (int)Math.round(masterHeight*4/6)) {
            skeletonLeftX += (x-oldX);
            masterLeftX += (x-oldX);
            redrawSkeletonPlot = true;
            redrawMasterPlot = true;
            oldX = x;
        }
        repaint();
        return true;
    }

    //reset this to the unlikely value for additional mouse drag events
    public boolean mouseUp(Event evt, int x, int y) {
        oldX = -1000000;
        return true;
    }

    public boolean mouseDown(Event evt, int x, int y) {
        //if within bottom two-thirds of skeleton plot
        if (x>=(int)Math.round(this.size().width/2-(this.size().width/2-skeletonLeftX)*graphScale/2+graphEmptySpace) &&
            x<=(int)Math.round(this.size().width/2-(this.size().width/2-skeletonLeftX)*graphScale/2+graphEmptySpace+
                numberOfSkeletonLines*lineSpacing*graphScale) &&
            y>=skeletonUpperY+(int)Math.round(skeletonHeight/3) && y<=skeletonUpperY+skeletonHeight) {
            if (draw) {
                //if normal marks
                if (markTypeInt == 1) {
                    skeletonNormalBoneX[numberOfSkeletonNormalBones] = (x-(int)Math.round(this.size().width/2-
                    (this.size().width/2-skeletonLeftX)*graphScale/2))/(lineSpacing*graphScale);
                    skeletonNormalBoneY[numberOfSkeletonNormalBones] = (skeletonUpperY+skeletonHeight-y)/
                    (lineSpacing*graphScale);
                    numberOfSkeletonNormalBones++;
                }
                //else if missing marks
                else if (markTypeInt == 2) {
                    skeletonMissingBoneX[numberOfSkeletonMissingBones] = (x-(int)Math.round(this.size().width/2-
                    (this.size().width/2-skeletonLeftX)*graphScale/2))/(lineSpacing*graphScale);
                    numberOfSkeletonMissingBones++;
                }
                //else if false marks
                else if (markTypeInt == 3) {
                    skeletonFalseBoneX[numberOfSkeletonFalseBones] = (x-(int)Math.round(this.size().width/2-
                    (this.size().width/2-skeletonLeftX)*graphScale/2))/(lineSpacing*graphScale);
                    numberOfSkeletonFalseBones++;
                }
                //else are wide marks
                else {
                    skeletonWideBoneX[numberOfSkeletonWideBones] = (x-(int)Math.round(this.size().width/2-
                    (this.size().width/2-skeletonLeftX)*graphScale/2))/(lineSpacing*graphScale);
                    numberOfSkeletonWideBones++;
                }
            }
            //else erase marks on skeleton plot
            else {
                //start looking in normal marks
                int boneCount = 0;
                while (((skeletonNormalBoneX[boneCount]-1) > (x-(int)Math.round(this.size().width/2-
                    (this.size().width/2-skeletonLeftX)*graphScale/2))/(lineSpacing*graphScale) ||
                    (skeletonNormalBoneX[boneCount]+1) < (x-(int)Math.round(this.size().width/2-
                    (this.size().width/2-skeletonLeftX)*graphScale/2))/(lineSpacing*graphScale)) &&
                    (boneCount < numberOfSkeletonNormalBones)) boneCount++;
                while (boneCount < (numberOfSkeletonNormalBones-1)) {
                    skeletonNormalBoneX[boneCount] = skeletonNormalBoneX[boneCount+1];
                    skeletonNormalBoneY[boneCount] = skeletonNormalBoneY[boneCount+1];
                    boneCount++;
                }
                if (boneCount == numberOfSkeletonNormalBones-1) numberOfSkeletonNormalBones--;
                else {
                    //continue looking in missing marks
                    boneCount = 0;
                    while (((skeletonMissingBoneX[boneCount]-1) > (x-(int)Math.round(this.size().width/2-
                        (this.size().width/2-skeletonLeftX)*graphScale/2))/(lineSpacing*graphScale) ||
                        (skeletonMissingBoneX[boneCount]+1) < (x-(int)Math.round(this.size().width/2-
                        (this.size().width/2-skeletonLeftX)*graphScale/2))/(lineSpacing*graphScale)) &&
                        (boneCount < numberOfSkeletonMissingBones)) boneCount++;
                    while (boneCount < (numberOfSkeletonMissingBones-1)) {
                        skeletonMissingBoneX[boneCount] = skeletonMissingBoneX[boneCount+1];
                        boneCount++;
                    }
                    if (boneCount == numberOfSkeletonMissingBones-1) numberOfSkeletonMissingBones--;
                    else {
                        //continue looking in false marks
                        boneCount = 0;
                        while (((skeletonFalseBoneX[boneCount]-1) > (x-(int)Math.round(this.size().width/2-
                            (this.size().width/2-skeletonLeftX)*graphScale/2))/(lineSpacing*graphScale) ||
                            (skeletonFalseBoneX[boneCount]+1) < (x-(int)Math.round(this.size().width/2-
                            (this.size().width/2-skeletonLeftX)*graphScale/2))/(lineSpacing*graphScale)) &&
                            (boneCount < numberOfSkeletonFalseBones)) boneCount++;
                        while (boneCount < (numberOfSkeletonFalseBones-1)) {
                            skeletonFalseBoneX[boneCount] = skeletonFalseBoneX[boneCount+1];
                            boneCount++;
                        }
                        if (boneCount == numberOfSkeletonFalseBones-1) numberOfSkeletonFalseBones--;
                        else {
                            //continue looking in wide marks
                            boneCount = 0;
                            while (((skeletonWideBoneX[boneCount]-1) > (x-(int)Math.round(this.size().width/2-
                                (this.size().width/2-skeletonLeftX)*graphScale/2))/(lineSpacing*graphScale) ||
                                (skeletonWideBoneX[boneCount]+1) < (x-(int)Math.round(this.size().width/2-
                                (this.size().width/2-skeletonLeftX)*graphScale/2))/(lineSpacing*graphScale)) &&
                                (boneCount < numberOfSkeletonWideBones)) boneCount++;
                            while (boneCount < (numberOfSkeletonWideBones-1)) {
                                skeletonWideBoneX[boneCount] = skeletonWideBoneX[boneCount+1];
                                boneCount++;
                            }
                            if (boneCount == numberOfSkeletonWideBones-1) numberOfSkeletonWideBones--;
                        }
                    }
                }
            }
            redrawSkeletonPlot = true;
        }
        //else if within top third of master plot
        else
        if (x>=(int)Math.round(this.size().width/2-(this.size().width/2-masterLeftX)*graphScale/2+graphEmptySpace) &&
            x<=(int)Math.round(this.size().width/2-(this.size().width/2-masterLeftX)*graphScale/2+graphEmptySpace+
            numberOfMasterLines*lineSpacing*graphScale) &&
            y>=masterUpperY && y<=masterUpperY+(int)Math.round(masterHeight*2/6)) {
            if (draw) {
                if (markTypeInt == 1) {
                    masterBoneX[numberOfMasterBones] = (x-(int)Math.round(this.size().width/2-(this.size().width/2-
                    masterLeftX)*graphScale/2))/(lineSpacing*graphScale);
                    masterBoneY[numberOfMasterBones] = (y-masterUpperY)/(lineSpacing*graphScale);
                    numberOfMasterBones++;
                }
                //else are wide marks
                else
                if (markTypeInt == 4) {
                    masterWideBoneX[numberOfMasterWideBones] = (x-(int)Math.round(this.size().width/2-(this.size().width/2-
                    masterLeftX)*graphScale/2))/(lineSpacing*graphScale);
                    numberOfMasterWideBones++;
                }
            }
            else { //erase marks on master plot
                int boneCount = 0;
                while (((masterBoneX[boneCount]-1) > (x-(int)Math.round(this.size().width/2-(this.size().width/2-
                    masterLeftX)*graphScale/2))/(lineSpacing*graphScale) || (masterBoneX[boneCount]+1) <
                    (x-(int)Math.round(this.size().width/2-(this.size().width/2-masterLeftX)*graphScale/2))/
                    (lineSpacing*graphScale)) && (boneCount < numberOfMasterBones)) boneCount++;
                while (boneCount < (numberOfMasterBones-1)) {
                    masterBoneX[boneCount] = masterBoneX[boneCount+1];
                    masterBoneY[boneCount] = masterBoneY[boneCount+1];
                    boneCount++;
                }
                if (boneCount == numberOfMasterBones-1) numberOfMasterBones--;
                else {
                    boneCount = 0;
                    while (((masterWideBoneX[boneCount]-1) > (x-(int)Math.round(this.size().width/2-(this.size().width/2-
                        masterLeftX)*graphScale/2))/(lineSpacing*graphScale) || (masterWideBoneX[boneCount]+1) <
                        (x-(int)Math.round(this.size().width/2-(this.size().width/2-masterLeftX)*graphScale/2))/
                        (lineSpacing*graphScale)) && (boneCount < numberOfMasterWideBones)) boneCount++;
                    while (boneCount < (numberOfMasterWideBones-1)) {
                        masterWideBoneX[boneCount] = masterWideBoneX[boneCount+1];
                        boneCount++;
                    }
                    if (boneCount == numberOfMasterWideBones-1) numberOfMasterWideBones--;
                }
            }
            redrawMasterPlot = true;
        }
        repaint();
        return true;
    }

    //override update to stop clear screen and eliminate flicker
    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        if (redrawCore) {
            offscreenG.setColor(myBeige);
            offscreenG.fillRect(0,0,this.size().width, coreMountUpperY+coreMountHeight+16);
            offscreenG.setColor(myCoreMount);
            offscreenG.fillRect((int)Math.round(this.size().width/2-(this.size().width/2-coreMountLeftX)*coreMagLevel),
                                 coreMountUpperY,(coreLength+2*coreMountEdge)*coreMagLevel,coreMountHeight);
            //draw core boundary
            offscreenG.setColor(Color.black);
            offscreenG.drawRect((int)Math.round(this.size().width/2-(this.size().width/2-coreMountLeftX)*coreMagLevel)+
                                 coreMountEdge*coreMagLevel,coreMountUpperY+coreMountEdge,coreLength*coreMagLevel,
                                 coreHeight);
            //draw rings a vertical line at a time, skipping absent rings
            int absentRings = 0;
            int numberOfFalseRings = 0;
            int currentXPosition = 0;
            int ring = 0;
            while ((int)Math.round(this.size().width/2-(this.size().width/2-coreMountLeftX)*
                coreMagLevel+(coreMountEdge+currentXPosition)*coreMagLevel) < -100) {
                int divisor = 1;
                if (ring == falseRing) {
                    divisor = 2;
                    numberOfFalseRings++;
                }
                while ((index[ring+absentRings+(int)Math.round(numberOfMasterRings*coreStartPosition)] < 0.06) &&
                    (ring > 0) && (ring < (numberOfRings-1))) {
                    absentRings++;
                }
                currentXPosition += (int)Math.round(index[ring+absentRings+(int)Math.round(numberOfMasterRings*
                                     coreStartPosition)]/2*targetRingWidth/divisor);
                ring++;
            }
            while ((int)Math.round(this.size().width/2-(this.size().width/2-coreMountLeftX)*coreMagLevel+
                (coreMountEdge+currentXPosition)*coreMagLevel) < (this.size().width+100) && ring < numberOfRings - numberOfFalseRings) {
                int divisor = 1;
                if (ring == falseRing) {
                    divisor = 2;
                    numberOfFalseRings++;
                }
                while ((index[ring+absentRings+(int)Math.round(numberOfMasterRings*coreStartPosition)] < 0.06) &&
                    (ring > 0) && (ring < (numberOfRings-1))) {
                    absentRings++;
                }
                for (int numberOfDraws = 1; numberOfDraws < (divisor+1); numberOfDraws++) {
                    //draw first black line
                    offscreenG.setColor(Color.black);
                    offscreenG.fillRect((int)Math.round(this.size().width/2-(this.size().width/2-coreMountLeftX)*
                                         coreMagLevel+(coreMountEdge+currentXPosition)*coreMagLevel),
                                         coreMountUpperY + coreMountEdge+1,1,coreHeight-1);
                    //draw earlywood
                    offscreenG.setColor(myEarlywood);
                    offscreenG.fillRect((int)Math.round(this.size().width/2-(this.size().width/2-coreMountLeftX)*
                                         coreMagLevel+(coreMountEdge+currentXPosition)*coreMagLevel)+1,
                                         coreMountUpperY + coreMountEdge + 1,
                                        (int)Math.round(.75*index[ring+absentRings+(int)Math.round(numberOfMasterRings*
                                         coreStartPosition)]/2*targetRingWidth*coreMagLevel/divisor)+coreMagLevel-2,
                                         coreHeight-1);
                    currentXPosition += (int)Math.round(.75*index[ring+absentRings+(int)Math.round(numberOfMasterRings*
                                         coreStartPosition)]/2*targetRingWidth/divisor);
                    //draw latewood
                    if (latewoodColor[ring] == 1) offscreenG.setColor(myLightLatewood);
                    else if (latewoodColor[ring] == 2) offscreenG.setColor(myMediumLatewood);
                    else offscreenG.setColor(myDarkLatewood);
                    offscreenG.fillRect((int)Math.round(this.size().width/2-(this.size().width/2-coreMountLeftX)*
                                         coreMagLevel+(coreMountEdge+currentXPosition)*coreMagLevel),
                                         coreMountUpperY + coreMountEdge + 1,
                                       ((int)Math.round(index[ring+absentRings+(int)Math.round(numberOfMasterRings*
                                         coreStartPosition)]/2*targetRingWidth)-(int)Math.round(.75*
                                         index[ring+absentRings+(int)Math.round(numberOfMasterRings*coreStartPosition)]
                                         /2*targetRingWidth))*coreMagLevel/divisor,coreHeight-1);
                    currentXPosition +=((int)Math.round(index[ring+absentRings+(int)Math.round(numberOfMasterRings*
                                         coreStartPosition)]/2*targetRingWidth/divisor)-(int)Math.round(.75*
                                         index[ring+absentRings+(int)Math.round(numberOfMasterRings*coreStartPosition)]
                                         /2*targetRingWidth/divisor));
                }
                // put a filled dot on every 10th visible ring and label it
                if ((ring+numberOfFalseRings) % 10 == 0 && ring != 0) {
                    offscreenG.setColor(Color.black);
                    offscreenG.fillOval((int)Math.round(this.size().width/2-(this.size().width/2-coreMountLeftX)*
                                         coreMagLevel+(coreMountEdge+currentXPosition)*coreMagLevel-
                                         index[ring+absentRings+(int)Math.round(numberOfMasterRings*
                                         coreStartPosition)]/2*targetRingWidth/2*coreMagLevel)-2,
                                         coreMountUpperY+coreMountEdge+(int)Math.round(coreHeight/2)-2,4,4);
                    offscreenG.setFont(fbsmall);
                    offscreenG.drawString(""+(ring+numberOfFalseRings),
                                         (int)Math.round(this.size().width/2-(this.size().width/2-coreMountLeftX)*
                                          coreMagLevel+(coreMountEdge+currentXPosition)*coreMagLevel-
                                          index[ring+absentRings+(int)Math.round(numberOfMasterRings*
                                          coreStartPosition)]/2*targetRingWidth/2*coreMagLevel)-
                                         (""+(ring+numberOfFalseRings)).length()*4,
                                          coreMountUpperY + coreMountHeight);
                }
                else if (ring == 0) {
                    // put an open 4-pixel diameter circle on 0th ring
                    offscreenG.setColor(Color.black);
                    offscreenG.drawOval((int)Math.round(this.size().width/2-(this.size().width/2-coreMountLeftX)*
                                         coreMagLevel+(coreMountEdge+index[(int)Math.round(numberOfMasterRings*
                                         coreStartPosition)]/2*targetRingWidth/2)*coreMagLevel)-2,
                                         coreMountUpperY + coreMountEdge+(int)Math.round(coreHeight/2) - 2,4,4);
                }
                ring++;
            }
            //show hint
            if (showHint) {
                offscreenG.setColor(Color.black);
                offscreenG.drawString("Hint: Core is missing "+absentRingsHint+" ring(s) and has "+numberOfFalseRings+
                " false ring(s).",this.size().width/2-("Hint: Core is missing "+absentRings+" ring(s) and has "+
                numberOfFalseRingsHint+" false ring(s).").length()*2,skeletonUpperY-3);
            }
            currentXPosition = 0;
            redrawCore = false;
        }
        if (redrawSkeletonPlot) {
            offscreenG.setColor(myBeige);
            offscreenG.fillRect(0,skeletonUpperY,this.size().width, skeletonHeight + 1);
            //draw graph paper for skeleton plot
            //draw white backing
            offscreenG.setColor(Color.white);
            offscreenG.fillRect(Math.max((int)Math.round(this.size().width/2-(this.size().width/2-skeletonLeftX)*
                                graphScale/2),0),
                                skeletonUpperY,
                               (int)Math.round(Math.min(this.size().width,this.size().width/2-
                               (this.size().width/2-skeletonLeftX)*graphScale/2+graphEmptySpace+
                                numberOfSkeletonLines*lineSpacing*graphScale) -
                                Math.max(this.size().width/2-(this.size().width/2-skeletonLeftX)*
                                graphScale/2,0)),
                                skeletonHeight + 1);
            //draw horizontal lines
            for (int horizontalLine = 0; horizontalLine < 16; horizontalLine++) {
                if (horizontalLine % 5 != 0) offscreenG.setColor(myLightGreen);
                else offscreenG.setColor(myDarkGreen);
                offscreenG.drawLine(Math.max((int)Math.round(this.size().width/2-(this.size().width/2-skeletonLeftX)*
                                    graphScale/2+graphEmptySpace),0),
                                    skeletonUpperY+(int)Math.round(horizontalLine*lineSpacing*graphScale),
                                    Math.min(this.size().width,(int)Math.round(this.size().width/2-(this.size().width/2-
                                    skeletonLeftX)*graphScale/2+graphEmptySpace+numberOfSkeletonLines*
                                    lineSpacing*graphScale)),
                                    skeletonUpperY+(int)Math.round(horizontalLine*lineSpacing*graphScale));
            }
            //draw vertical lines
            for (int verticalLine = Math.max((int)Math.round((this.size().width/2-(this.size().width/2-skeletonLeftX)*
                                    graphScale/2+graphEmptySpace)/-(lineSpacing*graphScale)),0);
                     verticalLine <(int)Math.round((Math.min(this.size().width,this.size().width/2-(this.size().width/2-
                                    skeletonLeftX)*graphScale/2+graphEmptySpace+numberOfSkeletonLines*lineSpacing*
                                    graphScale)-Math.max(this.size().width/2-(this.size().width/2-skeletonLeftX)*
                                    graphScale/2+graphEmptySpace,0))/(lineSpacing*graphScale)) +
                                    Math.max((int)Math.round((this.size().width/2-(this.size().width/2-skeletonLeftX)*
                                    graphScale/2+graphEmptySpace)/-(lineSpacing*graphScale)),0);
                     verticalLine ++) {
                if (verticalLine % 5 != 0) offscreenG.setColor(myLightGreen);
                else offscreenG.setColor(myDarkGreen);
                offscreenG.drawLine((int)Math.round(this.size().width/2-(this.size().width/2-skeletonLeftX)*
                                     graphScale/2+graphEmptySpace+verticalLine*lineSpacing*graphScale),
                                     skeletonUpperY+1,
                                    (int)Math.round(this.size().width/2-(this.size().width/2-skeletonLeftX)*
                                     graphScale/2+graphEmptySpace+verticalLine*lineSpacing*graphScale),
                                     skeletonUpperY+skeletonHeight-1);
                //write in decade labels
                if ((verticalLine-2)%10 == 0) {
                    offscreenG.setColor(Color.black);
                    if (graphScale == 2) offscreenG.setFont(fbi);
                    else offscreenG.setFont(fbismall);
                    offscreenG.drawString(""+(verticalLine-2),
                                         (int)Math.round(this.size().width/2-(this.size().width/2-skeletonLeftX)*
                                          graphScale/2+graphEmptySpace+(verticalLine-2)*lineSpacing*graphScale-
                                         (""+(verticalLine-2)).length()*2*graphScale),
                                          skeletonUpperY+(int)Math.round(skeletonHeight/3)-2);
                }
            }
            //draw in various  bones of skeleton plot
            offscreenG.setColor(Color.black);
            for (int bone = 0; bone < numberOfSkeletonNormalBones; bone++)
                offscreenG.fillRect((int)Math.round(this.size().width/2-(this.size().width/2-skeletonLeftX)*
                                     graphScale/2+skeletonNormalBoneX[bone]*graphScale*lineSpacing)-1,
                                     skeletonUpperY + skeletonHeight - (int)Math.round(skeletonNormalBoneY[bone]*graphScale*
                                     lineSpacing),3,
                                    (int)Math.round(skeletonNormalBoneY[bone]*graphScale*lineSpacing));
            for (int bone = 0; bone < numberOfSkeletonMissingBones; bone++) {
                for (int segment = 0; segment < 3; segment++)
                     offscreenG.fillRect((int)Math.round(this.size().width/2-(this.size().width/2-skeletonLeftX)*
                                          graphScale/2+skeletonMissingBoneX[bone]*graphScale*lineSpacing),
                                          skeletonUpperY + (int)Math.round(skeletonHeight/3) + (int)Math.round(segment*4*graphScale*
                                          lineSpacing),2,
                                          (int)Math.round(graphScale*lineSpacing*2));
                if (graphScale == 2) offscreenG.setFont(fb);
                else offscreenG.setFont(fbsmall);
                offscreenG.drawString(">",(int)Math.round(this.size().width/2-(this.size().width/2-skeletonLeftX)*
                                     graphScale/2+skeletonMissingBoneX[bone]*graphScale*lineSpacing)-3,
                                     skeletonUpperY + (int)Math.round(graphScale*lineSpacing*1.6));
            }
            for (int bone = 0; bone < numberOfSkeletonFalseBones; bone++) {
                for (int segment = 0; segment < 3; segment++)
                    for (int lineWeight = 0; lineWeight < 2; lineWeight++)
                        offscreenG.drawLine((int)Math.round(this.size().width/2-(this.size().width/2-skeletonLeftX)*
                                             graphScale/2+skeletonFalseBoneX[bone]*graphScale*lineSpacing - graphScale*lineSpacing/2),
                                             skeletonUpperY + (int)Math.round(skeletonHeight/3) + (int)Math.round(segment*4*graphScale*
                                             lineSpacing) + lineWeight,
                                             (int)Math.round(this.size().width/2-(this.size().width/2-skeletonLeftX)*
                                             graphScale/2+skeletonFalseBoneX[bone]*graphScale*lineSpacing - graphScale*lineSpacing/2) + (int)Math.round(graphScale*lineSpacing),
                                             skeletonUpperY + (int)Math.round(skeletonHeight/3) + (int)Math.round(segment*4*graphScale*
                                             lineSpacing)+(int)Math.round(graphScale*lineSpacing*2)+lineWeight);
                if (graphScale == 2) offscreenG.setFont(fb);
                else offscreenG.setFont(fbsmall);
                offscreenG.drawString("<",(int)Math.round(this.size().width/2-(this.size().width/2-skeletonLeftX)*
                                     graphScale/2+skeletonFalseBoneX[bone]*graphScale*lineSpacing)-3,
                                     skeletonUpperY + (int)Math.round(graphScale*lineSpacing*1.6));
            }
            offscreenG.setColor(Color.black);
            if (graphScale == 2) offscreenG.setFont(fb);
            else offscreenG.setFont(fbsmall);
            for (int bone = 0; bone < numberOfSkeletonWideBones; bone++)
                offscreenG.drawString("b",(int)Math.round(this.size().width/2-(this.size().width/2-skeletonLeftX)*
                                     graphScale/2+skeletonWideBoneX[bone]*graphScale*lineSpacing)-3,
                                     skeletonUpperY + (int)Math.round(skeletonHeight/3) + (int)Math.round(graphScale*lineSpacing*2));
            redrawSkeletonPlot = false;
        }
        if (redrawMasterPlot) {
            offscreenG.setColor(myBeige);
            offscreenG.fillRect(0,masterUpperY,this.size().width, this.size().height - masterUpperY);
            //draw graph paper for master plot
            if (showMaster) {
                //draw white backing
                offscreenG.setColor(Color.white);
                double xOffset = this.size().width/2-(this.size().width/2-masterLeftX)*graphScale/2 + graphEmptySpace;
                double squareSize = lineSpacing*graphScale;
                int startSquare = Math.max((int)Math.round(-xOffset/squareSize), 0);
                offscreenG.fillRect(Math.max((int)Math.round(xOffset-graphEmptySpace),0),
                                    masterUpperY,
                                    (int)Math.round(Math.min(this.size().width,
                                                       xOffset+numberOfMasterLines*squareSize) -
                                                          Math.max(xOffset-graphEmptySpace,0)),
                                    masterHeight + 1);
                //draw horizontal lines
                for (int horizontalLine = 0; horizontalLine < 31; horizontalLine++) {
                    if (horizontalLine % 5 != 0) offscreenG.setColor(myLightGreen);
                    else offscreenG.setColor(myDarkGreen);
                    offscreenG.drawLine(Math.max((int)Math.round(xOffset),0),
                                        masterUpperY+(int)Math.round(horizontalLine*squareSize),
                                        Math.min(this.size().width, (int)Math.round(xOffset+numberOfMasterLines*squareSize)),
                                        masterUpperY+(int)Math.round(horizontalLine*squareSize));
                }
                //draw vertical lines
                for (int verticalLine = startSquare;
                         verticalLine < (int)Math.round((Math.min(this.size().width, xOffset+numberOfMasterLines*squareSize)
                                                       - Math.max(xOffset,0))
                                                       / squareSize)
                                        + startSquare;
                         verticalLine ++) {
                    if (verticalLine % 5 != 0) offscreenG.setColor(myLightGreen);
                    else offscreenG.setColor(myDarkGreen);
                    offscreenG.drawLine((int)Math.round(xOffset+verticalLine*squareSize),
                                         masterUpperY+1,
                                        (int)Math.round(xOffset+verticalLine*squareSize),
                                         masterUpperY+masterHeight-1);
                    //draw time-series plot of index data
                    if ((verticalLine > startSquare) && (verticalLine < numberOfMasterRings)) {
                        offscreenG.setColor(Color.black);
                        offscreenG.drawLine((int)Math.round(xOffset+(verticalLine-1)*squareSize),
                                            (int)Math.round(masterUpperY+masterHeight*(1-index[verticalLine-1]/3)),
                                            (int)Math.round(xOffset+verticalLine*squareSize),
                                            (int)Math.round(masterUpperY+masterHeight*(1-index[verticalLine]/3)));
                                        }
                    //write decade labels
                    boolean isBC = false;
                    if ((masterStartYear+verticalLine-2)%10 == 0) {
                        if ((masterStartYear+verticalLine-2) < 0) isBC = true;
                        offscreenG.setColor(Color.black);
                        if (graphScale == 2) offscreenG.setFont(fbi);
                        else offscreenG.setFont(fbismall);
                        String yearString;
                        if ((masterStartYear+verticalLine-2)%100 != 0)
                             yearString = (""+(masterStartYear+verticalLine-2)).substring((""+(masterStartYear+verticalLine-2)).length()-2);
                        else yearString = (""+(masterStartYear+verticalLine-2));
                        if (isBC) yearString = "-"+yearString;
                        else if ((masterStartYear+verticalLine-2) == 0) yearString = "BC "+yearString+" AD";
                        offscreenG.drawString(yearString,
                                             (int)Math.round(xOffset+(verticalLine-2)*squareSize-yearString.length()*2*graphScale),
                                              masterUpperY+(int)Math.round(masterHeight/3)+16);
                    }
                }
                //draw two black horizontal lines
                offscreenG.setColor(Color.black);
                for (int numerator = 2; numerator < 5; numerator += 2)
                    offscreenG.fillRect(Math.max((int)Math.round(xOffset),0),
                                        masterUpperY+(int)Math.round(masterHeight*numerator/6) - 1,
                                        (int)Math.round(Math.min(this.size().width,
                                                                 xOffset+numberOfMasterLines*squareSize)
                                                       -Math.max(xOffset,0)),
                                        3);
                //write y-axis 1.0 label
                if (graphScale == 2) offscreenG.setFont(fb);
                else offscreenG.setFont(fbsmall);
                if ((int)Math.round(xOffset) > 0)
                    offscreenG.drawString("1.0",
                                         (int)Math.round(xOffset-30*graphScale/2),
                                          masterUpperY+(int)Math.round(masterHeight*2/3)+6);
                //draw in various bones of master plot
                offscreenG.setColor(Color.black);
                for (int bone = 0; bone < numberOfMasterBones; bone++)
                    offscreenG.fillRect((int)Math.round(xOffset-graphEmptySpace+masterBoneX[bone]*squareSize)-1,
                                        masterUpperY,
                                        3,
                                        (int)Math.round(masterBoneY[bone]*squareSize));
                //draw in b bones of master plot
                offscreenG.setColor(Color.black);
                if (graphScale == 2) offscreenG.setFont(fb);
                else offscreenG.setFont(fbsmall);
                for (int bone = 0; bone < numberOfMasterWideBones; bone++)
                    offscreenG.drawString("b",(int)Math.round(xOffset-graphEmptySpace+masterWideBoneX[bone]*squareSize)-3,
                                        masterUpperY+(int)Math.round(masterHeight/3)-3);
            }
            redrawMasterPlot = false;
        }
        //mark midpoint of applet width space across core series
        offscreenG.setColor(Color.blue);
        offscreenG.fillRect((int)Math.round(this.size().width/2)-1, coreMountUpperY, 3, coreMountEdge);
        offscreenG.fillRect((int)Math.round(this.size().width/2)-1,coreMountUpperY+coreMountEdge+coreHeight,3,
                             coreMountEdge);

        //other details
        offscreenG.setColor(Color.black);
        offscreenG.setFont(fb);
        //show answer
        if (showAnswers) {
            offscreenG.setColor(myBeige);
            offscreenG.fillRect(answersLeftX,answersUpperY,(int)Math.round(Math.max(answers[0].length(),(Math.max(answers[2].length(),answers[3].length())))*8),(answers.length+1)*20);
            offscreenG.setColor(Color.black);
            offscreenG.drawRect(answersLeftX,answersUpperY,(int)Math.round(Math.max(answers[0].length(),(Math.max(answers[2].length(),answers[3].length())))*8),(answers.length+1)*20);
            offscreenG.setFont(fb);
            for (int answerLine = 0; answerLine < answers.length; answerLine++)
                offscreenG.drawString(""+answers[answerLine], answersLeftX+10, answersUpperY + (answerLine + 1)*20);
        }

        //draw to onscreen
        g.drawImage(offscreenImg,0,0,this);
    }
}