import os
import csv
from datetime import datetime, timedelta



inputFile = "Extended_Data_With_6_Sensors_TSV.csv"
max_listing = 10


#ListEntry
def printSomeEntrys():
    global max_listing, inputFile
    with open(inputFile, newline='') as csvfile:
        reader = csv.reader(csvfile, delimiter=';', quotechar='|')
        count = 0
        for row in reader:
            if(count < max_listing):
                
                print(row[0])
            else:
                break
            count += 1
        
printSomeEntrys()
        