import gdal
import os
from optparse import OptionParser 

################################################################
def doit(opts, args):

    if opts.inF:
        myFList = [opts.inF]
    elif opts.inDir:
        # change directory to required one
        os.chdir(opts.inDir)
        myList = os.listdir('.')
        myFList = []
        for l in myList:
            if os.path.splitext(l)[1].lower() in ['.tif', '.asc']:
                myFList.append(l)

    if opts.outF:
        myOut = open(opts.outF,'w')
        if opts.inDir:
            myOut.write('# drillPoint.py: Examining dir %s at point X=%s Y=%s\n' %(opts.inDir, opts.long, opts.lat))
            
    for f in myFList:
        myF = gdal.Open(f)
        myA = myF.ReadAsArray()
        # get X and Y lower-left corner
        minX = myF.GetGeoTransform()[0]
        minY = myF.GetGeoTransform()[3]
        cellSizeX = myF.GetGeoTransform()[1]
        cellSizeY = myF.GetGeoTransform()[5]
        maxX = minX + (cellSizeX * myF.RasterXSize)
        maxY = minY + (cellSizeY * myF.RasterYSize)

        # work out difference from corner to chosen cell
        myXdiff = opts.long - minX
        myYdiff = opts.lat - minY

        # divide this difference by cell size to get cell position
        myXcell = int(myXdiff / cellSizeX) 
        myYcell = int(myYdiff / cellSizeY) 

        myCellValue = myA[myYcell][myXcell]

        if opts.verbose:
            print '''reading file %s
            minX = %s; minY = %s
            maxX = %s; maxY = %s
            cell size = %s x %s
            ''' %(f, minX, minY, maxX, maxY, cellSizeX, cellSizeY)
            print '''reading point lat=%s long=%s
            myXdiff=%s, myYdiff=%s
            myXcell=%s, myYcell=%s
            ''' %(opts.lat, opts.long,myXdiff,myYdiff,myXcell,myYcell)
            print 'matrix is len(myA)=%s, len(myA[0])=%s' %(len(myA), len(myA[0]))
            print '''cell value
            myCellValue=%s
            ''' %myCellValue
                            
        if opts.outF:
            if myCellValue > opts.value and myCellValue != opts.NoData:
                myOut.write('keeping\t%s\tvalue\t%s\n' %(f,myCellValue))
            else:
                myOut.write('rejecting\t%s\tvalue\t%s\n' %(f,myCellValue))
        else:
            if myCellValue > opts.value and myCellValue != opts.NoData:
                print 'keeping\t%s\tvalue\t%s\n' %(f,myCellValue)
            else:
                print 'rejecting\t%s\tvalue\t%s\n' %(f,myCellValue)
            
    if opts.outF:
        myOut.close()
        
    return


################################################################  
def main():
    usage = 'Usage: drillPoint.py [opts]'
    parser = OptionParser(usage)
    
    parser.add_option("-v", "--verbose",
                      dest="verbose", action="store_true",
                      help="Print more detail")
    parser.add_option("--debug",
                      dest="debug", action="store_true",
                      help="Print debug detail")
    parser.add_option("-y", "--lat", dest="lat", default=0,
                      type="float", help="Latitude of point to drill")
    parser.add_option("-x", "--long", dest="long", default=0,
                      type="float", help="Longitude of point to drill")
    parser.add_option("-c", "--CompareValue", dest="value", default=0,
                      type="float", help="Compare drilled cell to this value")
    parser.add_option("-i", "--infile", dest="inF", 
                      help="input file to process")
    parser.add_option("-d", "--directory", dest="inDir", 
                      help="process all gdal files in this directory")
    parser.add_option("-o", "--outFile", dest="outF", default=None,
                      help="output text file")
    parser.add_option("--NoData", dest="NoData", default=255,
                      help="Ignore no data values of this number")
    
    (opts, args) = parser.parse_args()
    
    # Run algorithm
    doit(opts, args)
        
if __name__ == "__main__":
    main()
