import os
import csv
from datetime import datetime, timedelta


inputFile = 'Basic_Data_With_6_Sensors.csv'
inputFileSeperator = "\t"
def initInputFile():
    global inputFile, inputFileSeperator
    if(inputFileSeperator == "\t"):
        inputFile = 'Basic_Data_With_6_Sensors_TSV.csv'
    if(inputFileSeperator == ";"):
        inputFile = 'Basic_Data_With_6_Sensors_CSV.csv'
    if(inputFileSeperator == ","):
        inputFile = 'Basic_Data_With_6_Sensors_COMMA.csv'
initInputFile()

exportFile = 'Extended_Data_With_6_Sensors.csv'
outputFileSeperator = "\t"
def initExportFile():
    global exportFile, outputFileSeperator
    if(outputFileSeperator == "\t"):
        exportFile = 'Extended_Data_With_6_Sensors_TSV.csv'
    if(outputFileSeperator == ";"):
        exportFile = 'Extended_Data_With_6_Sensors_CSV.csv'
    if(outputFileSeperator == ","):
        exportFile = 'Extended_Data_With_6_Sensors_COMMA.csv'
initExportFile()

if(os.path.isfile(exportFile)):
    os.remove(exportFile)
    


currentDateTime = datetime(2020, 1, 1, 00, 00)
numberOfDays = 100
countCurrantDay = 0
datasetOneDay = []
header  = None

def loadDataSet():
    global inputFile, datasetOneDay, inputFileSeperator, header
    startDate = datetime(2022, 3, 4, 0,0).date()
    with open(inputFile, newline='') as csvfile:
        reader = csv.reader(csvfile, delimiter=inputFileSeperator, quotechar='|')
        count  = 0
        for row in reader:
            if(count == 0):
                header = row
                count+=1
                continue
            timestamp = int(row[0])/1000
            rowDateTime = datetime.fromtimestamp(timestamp)
            rowDate = rowDateTime.date()
            if(count == 1):
                #startDate = rowDate
                print(startDate)
            if(rowDate == startDate):
                datasetOneDay.append(row)
                
            
            count +=1
            
loadDataSet()

#print(datasetOneDay)
dataSetCounter = len(datasetOneDay)
#print(f"DataSet: {dataSetCounter}")


def generateTimeStame(date, time):
    
    datetime_object = datetime.strptime(f'{date} {time}', '%Y-%m-%d %H:%M:%S')
    timeStamp = int(datetime.timestamp(datetime_object)*1000)
    return timeStamp

def createExtendedFile():
    global exportFile, outputFileSeperator, currentDateTime, numberOfDays
    
    with open(exportFile, 'x') as csvfile:
        wreiter = csv.writer(csvfile, delimiter=outputFileSeperator, lineterminator='\n')
        
        while(currentDateTime.date() < datetime.now().date()):
            for row in datasetOneDay:
                writeInFile(wreiter, int(row[0]), row[1], row[2] )
                
            currentDateTime = currentDateTime + timedelta(days=1)
            print(currentDateTime)
                
def writeInFile(wreiter, inputTS, sensorid, value):
    global currentDateTime
    time = datetime.fromtimestamp(inputTS/1000).time()
    #outputTimestamp = datetime(currentDateTime.year, currentDateTime.month, currentDateTime.day, time.hour, time.min,time.second)
    outputTimestamp = generateTimeStame(currentDateTime.date(), time)
    wreiter.writerow([outputTimestamp, sensorid, value])
    
    
createExtendedFile()