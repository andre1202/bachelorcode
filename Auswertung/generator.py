import os
import csv
from datetime import datetime

max_listing = 20
inputFile = 'E2022-03-01_2022-03-09.csv'
outputFile = 'Basic_Data_With_6_Sensors_TSV.csv'


seperator = ";"

if(seperator == ";"):
    outputFile = 'Basic_Data_With_6_Sensors_CSV.csv'
elif(seperator== ","):
    outputFile = 'Basic_Data_With_6_Sensors_COMMA.csv'
    

sensor1_string = 'sensor1'
sensor2_string = 'sensor2'
sensor3_string = 'sensor3'
sensor4_string = 'sensor4'
sensor5_string = 'sensor5'
sensor6_string = 'sensor6'

#ListEntry
def printSomeEntrys():
    global max_listing, inputFile
    with open(inputFile, newline='') as csvfile:
        reader = csv.reader(csvfile, delimiter=';', quotechar='|')
        count = 0
        for row in reader:
            if(count < max_listing):
                
                print(row)
            else:
                break
            count += 1
        
#printSomeEntrys()


#Returns Time Stamp out of date String (YYYY-MM-DD) and time String (HH:MM:SS)
def generateTimeStame(date, time):
    
    datetime_object = datetime.strptime(f'{date} {time}', '%Y-%m-%d %H:%M:%S')
    timeStamp = int(datetime.timestamp(datetime_object)*1000)
    return timeStamp
      
def initHeader(header, writer):
    global sensor1_string
    global sensor2_string
    global sensor3_string
    global sensor4_string
    global sensor5_string
    global sensor6_string
    sensor1_string = header[2]
    sensor2_string = header[3]
    sensor3_string = header[4]
    sensor4_string = header[5]
    sensor5_string = header[6]
    sensor6_string = header[10]
    
    writer.writerow(['timestamp', 'sensorid', 'value'])
    
        
def processRow(row, writer):
    date = row[0]
    time = row[1]
    timeStamp = generateTimeStame(date, time)
    sensor1 = row[2].replace(',', '.')
    sensor2 = row[3].replace(',', '.')
    sensor3 = row[4].replace(',', '.')
    sensor4 = row[5].replace(',', '.')
    sensor5 = row[6].replace(',', '.')
    sensor6 = row[10].replace(',', '.')
    
    writer.writerow([timeStamp, sensor1_string, sensor1])
    writer.writerow([timeStamp, sensor2_string, sensor2])
    writer.writerow([timeStamp, sensor3_string, sensor3])
    writer.writerow([timeStamp, sensor4_string, sensor4])
    writer.writerow([timeStamp, sensor5_string, sensor5])
    writer.writerow([timeStamp, sensor6_string, sensor6])
    
    
    
    
#create new tsv File

def createOutputFile(seperator):
    global inputFile, outputFile
    #Remove Existing file
    if(os.path.isfile(outputFile)):
        os.remove(outputFile)
    
    #Create New File
    with open(outputFile, 'x') as tsvOutputfile:
        writer = csv.writer(tsvOutputfile, delimiter=seperator, lineterminator='\n')
        with open(inputFile, newline='') as csvInputFile:    
            reader = csv.reader(csvInputFile, delimiter=';', quotechar='|')
            count = 0
            for row in reader:
                if(count == 0):
                    initHeader(row, writer)
                    print(row)
                    count += 1
                else:
                    processRow(row, writer)
                    
createOutputFile(seperator)            
 