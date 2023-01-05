import os
import csv
import re
import TestHelper as TH


outputFile = "Auswertung7.csv"
tableFile = "Tabellen2.csv"

outputFileDuration = "Duration.csv"

outputFileaverage = "average.csv"

seperator = "\t"

header = "avg\thigh\tlow"
exportLines = []
exportTableLines = []
exportLines.append(header)

mainPath = "./monitoring/"
influxPath = mainPath + "influxDB/"
ownDBPath = mainPath + "OwnDB/"
timescaleDBPath = mainPath + "timeScaleDB/"


DBSetOwnDB = TH.DBTestSet("OwnDB")
DBSetTimeScaleDB = TH.DBTestSet("TimeScaleDB")
DBSetInfluxDB = TH.DBTestSet("InfluxDB")



def createTestSet():
    global DBSetOwnDB, DBSetTimeScaleDB, DBSetInfluxDB
    
    for file in os.listdir(ownDBPath):        
        fileStringWithoudDateAndeFileEnding = file[20 :].split(".")[0]
        splittedString = fileStringWithoudDateAndeFileEnding.split('_', 1)
        number = int(splittedString[0])
        testName = splittedString[1]
        filePath = f"{ownDBPath}{file}"
        DBSetOwnDB.addTest(testName, TH.createTest(filePath))
       
    for file in os.listdir(timescaleDBPath):
        fileStringWithoudDateAndeFileEnding = file[26 :].split(".")[0]
        
        splittedString = fileStringWithoudDateAndeFileEnding.split('_', 1)
        number = int(splittedString[0])
        testName = splittedString[1]
        filePath = f"{timescaleDBPath}{file}"
        DBSetTimeScaleDB.addTest(testName, TH.createTest(filePath))
       
    for file in os.listdir(influxPath):
        fileStringWithoudDateAndeFileEnding = file[23 :].split(".")[0]
        
        splittedString = fileStringWithoudDateAndeFileEnding.split('_', 1)
        number = int(splittedString[0])
        testName = splittedString[1]
        filePath = f"{influxPath}{file}"
        DBSetInfluxDB.addTest(testName, TH.createTest(filePath))

createTestSet()



def addLinesOfTestSet(testSet):
    name = testSet.name
    size = testSet.getSize()
    duration =round(testSet.getArytmetischesMittelTime(), 4)       
    durationstd = round(testSet.getStandardabweichungTime(),4)
    cpu_load_average, cpu_load_high, cpu_load_low = testSet.getArytmetischesMittelCpuLoad()
    std_cpu_load_average, std_cpu_load_high, std_cpu_load_low = testSet.getStandardabweichungCpuLoad()
    
    system_cpu_load_average, system_cpu_load_high,system_cpu_load_low = testSet.getArytmetischesMittelSystemCpuLoad()
    std_system_cpu_load_average, std_system_cpu_load_high, std_system_cpu_load_low = testSet.getStandardabweichungSystemCpuLoad()
    
    ram_load_average, ram_load_high, ram_load_low = testSet.getArytmetischesMittelRamLoad()
    std_ram_load_average, std_ram_load_high, std_ram_load_low = testSet.getStandardabweichungRamLoad()
    
    # exportLines.append(f"Name:\t{name}" )
    # exportLines.append(f"Size:\t{size}" )
    # exportLines.append(f"DurationMittel:\t{duration}\t DurationStandardAbweichung\t{durationstd}")
    # exportLines.append("cpu_load Mittel")
    # exportLines.append(f"{cpu_load_average}\t{cpu_load_high}\t{cpu_load_low}" )
    # exportLines.append("cpu_load StandardAbweichung")
    # exportLines.append(f"{std_cpu_load_average}\t{std_cpu_load_high}\t{std_cpu_load_low}" )
    # exportLines.append("system_cpu_load Mittel")
    # exportLines.append(f"{system_cpu_load_average}\t{system_cpu_load_high}\t{system_cpu_load_low}" )
    # exportLines.append("system_cpu_load StandardAbweichung")
    # exportLines.append(f"{std_system_cpu_load_average}\t{std_system_cpu_load_high}\t{std_system_cpu_load_low}" )
    # exportLines.append(f"ram_load Mittel")
    # exportLines.append(f"{ram_load_average}\t{ram_load_high}\t{ram_load_low}" )
    # exportLines.append("ram_load StandardAbweichung")
    # exportLines.append(f"{std_ram_load_average}\t{std_ram_load_high}\t{std_ram_load_low}" )

    
    # exportLines.append(f"{name} & {cpu_load_average} & {std_cpu_load_average} & {cpu_load_high} & {std_cpu_load_high} & {cpu_load_low} & {std_cpu_load_low} \\\\" )
    
    exportLines.append(f"{name} & {ram_load_average} & {std_ram_load_average} & {ram_load_high} & {std_ram_load_high} & {ram_load_low} & {std_ram_load_low} \\\\" )
    exportLines.append("\hline")




def createExportData():
    
    exportLines.append("OwnDB")
    
    for testSet in DBSetOwnDB.testSets:
        addLinesOfTestSet(testSet)
    
    
    exportLines.append("TimescaleDB")    
    
    for testSet in DBSetTimeScaleDB.testSets:
        addLinesOfTestSet(testSet)
    
    
    exportLines.append("InfluxDB")    
    
    for testSet in DBSetInfluxDB.testSets:
        addLinesOfTestSet(testSet)

#createExportData()


def createLatexTable():
    for ownDBtestSet in DBSetOwnDB.testSets:
        timescale = None
        influx = None
        for timescaleTestset in DBSetTimeScaleDB.testSets:
            if timescaleTestset.name == ownDBtestSet.name:
                timescale = timescaleTestset
        
        for influxTestSet in DBSetInfluxDB.testSets:
            if influxTestSet.name == ownDBtestSet.name:
                influx = influxTestSet
        addLinesTableOutput(ownDBtestSet, timescale, influx)
        pass


def addLinesTableOutput(ownDBTestSet, TimescaleTestSet, influxDBTestSet):
    name = ownDBTestSet.name
    exportTableLines.append(name)
    # exportTableLines.append("\\begin{tabular}[h]{|c|c|c|c|c|c|c|}")
    # exportTableLines.append("\hline")
    # exportTableLines.append( " & \multicolumn{2}{c|}{Durchschnitt} & \multicolumn{2}{c|}{Höchstwerte} & \multicolumn{2}{c|}{Tiefstwerte} \\\\")
    exportTableLines.append( ";Durchschnitt;;Höchstwerte;;Tiefstwerte")
    # exportTableLines.append("\hline")
    # exportTableLines.append("Datenbank & M & SD & M & SD & M & SD \\\\")
    exportTableLines.append("Datenbank;M;SD;M;SD;M;SD")
    # exportTableLines.append("\hline")
    exportTableLines.append(getTableLine(ownDBTestSet, "OwnDB"))
    # exportTableLines.append("\hline")
    exportTableLines.append(getTableLine(TimescaleTestSet, "Timescale"))
    # exportTableLines.append("\hline")
    if influxDBTestSet is not None: 
        exportTableLines.append(getTableLine(influxDBTestSet, "InfluxDB"))
        # exportTableLines.append("\hline")
        
    exportTableLines.append("")
    exportTableLines.append("")
    exportTableLines.append("")
    

def getTableLine(testSet, name):
    size = testSet.getSize()
    duration =round(testSet.getArytmetischesMittelTime(), 4)       
    durationstd = round(testSet.getStandardabweichungTime(),4)
    cpu_load_average, cpu_load_high, cpu_load_low = testSet.getArytmetischesMittelCpuLoad()
    std_cpu_load_average, std_cpu_load_high, std_cpu_load_low = testSet.getStandardabweichungCpuLoad()
    
    system_cpu_load_average, system_cpu_load_high,system_cpu_load_low = testSet.getArytmetischesMittelSystemCpuLoad()
    std_system_cpu_load_average, std_system_cpu_load_high, std_system_cpu_load_low = testSet.getStandardabweichungSystemCpuLoad()
    
    ram_load_average, ram_load_high, ram_load_low = testSet.getArytmetischesMittelRamLoad()
    std_ram_load_average, std_ram_load_high, std_ram_load_low = testSet.getStandardabweichungRamLoad()
    
    # return f"{name} & {duration} & {durationstd} & {system_cpu_load_average} & {std_system_cpu_load_average} & {ram_load_average} & {std_ram_load_average} \\\\";
    return f"{name};{duration};{durationstd};{system_cpu_load_average};{std_system_cpu_load_average};{ram_load_average};{std_ram_load_average}";

def createTableOutputfile():
    global tableFile
    #Remove Existing file
    if(os.path.isfile(tableFile)):
        os.remove(tableFile)
    
    #Create New File
    f = open(tableFile, "x")
    for line in exportTableLines:
            f.write(line)
            f.write("\n")
    f.close
    
createLatexTable()
createTableOutputfile()

def createOutputFile():
    global outputFile
    #Remove Existing file
    if(os.path.isfile(outputFile)):
        os.remove(outputFile)
    
    #Create New File
    f = open(outputFile, "x")
    for line in exportLines:
            f.write(line)
            f.write("\n")
    f.close

#createOutputFile()