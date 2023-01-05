import time
from datetime import datetime
import statistics
import csv

dateFormatter = '%Y-%m-%dT%H:%M:%S.%f'

def createTest(inputFile):
    lastline = ""
    
    avarage_cpu_load = 0.0
    lowest_cpu_load = 0.0
    highest_cpu_load = 0.0
    avarage_system_cpu_load = 0.0
    lowest_system_cpu_load = 0.0
    highest_system_cpu_load = 0.0
    average_ram_loads = 0.0
    lowest_ram_loads = 0.0
    highest_ram_loads = 0.0
    
    #In Miliseconds
    duration = 0.0
    lastline = []
    
    with open(inputFile, newline='') as csvfile:
        reader = csv.reader(csvfile, delimiter='\t', quotechar='|')        
        for row in reader:
            if "startTime" in row[0]:
                duration = getDuration(row)
            if(len(lastline) == 5  and lastline[0] == 'time'):
                if("Average" in lastline[4]):
                    
                    average_ram_loads = round(float(row[3]), 6)
                    avarage_cpu_load = round(float(row[1]), 6)     
                    avarage_system_cpu_load = round(float(row[2]), 6)
                if("Highest" in lastline[4]):
                    highest_ram_loads = round(float(row[3]), 6)
                    highest_cpu_load = round(float(row[1]), 6)          
                    highest_system_cpu_load = round(float(row[2]), 6)
                if("Lowest" in lastline[4]):
                    lowest_ram_loads = round(float(row[3]), 6)
                    lowest_cpu_load = round(float(row[1]), 6)          
                    lowest_system_cpu_load = round(float(row[2]), 6)
            lastline = row;
            
            
        cpu_load = Load(lowest_cpu_load, highest_cpu_load, avarage_cpu_load)
        system_cpu_load = Load(lowest_system_cpu_load, highest_system_cpu_load, avarage_system_cpu_load)
        ram_load =Load(lowest_ram_loads, highest_ram_loads, average_ram_loads)
        
        return Test(cpu_load, ram_load, system_cpu_load, duration)


def getDuration(row):
    
    if "duration" in row[2]:
        startDateString = row[2].replace('duration', '')[:26]
        endDateString = row[3][:26]
        stardDate = datetime.strptime(startDateString, dateFormatter)
        endDate = datetime.strptime(endDateString, dateFormatter)
        diff = stardDate - endDate
        diffInSeconds = abs(diff.total_seconds())
        
    return round(diffInSeconds, 4);


class Test:

    def __init__(self, cpu_load, ram_load, system_cpu_load, duration):
        # Load
        self.cpu_load = cpu_load
        self.ram_load = ram_load
        self.system_cpu_load = system_cpu_load
        # floats in seconds
        self.duration = duration
    

class Load:
    def __init__(self, min, max, average):
        self.max = max;
        self.min = min;
        self.average = average;


class TestSet:
    
    def __init__(self, name):
        self.name = name
        self.tests = []

    def getSize(self):
        return len(self.tests)

    def addTest(self, test):
        self.tests.append(test)
        
    def getArytmetischesMittelTime(self):
        count = 0
        sum = 0
        for test in self.tests:
            sum = sum + test.duration
            count = count + 1    
        return round(sum / count, 6)
          
    def getStandardabweichungTime(self):
        values = []
        for test in self.tests:
            values.append(test.duration)
        return  statistics.pstdev(values)
    

    def getArytmetischesMittelCpuLoad(self):
        count = 0
        sum_high = 0
        sum_low = 0
        sum_average = 0
        for test in self.tests:
            sum_average = sum_average + test.cpu_load.average
            sum_low = sum_low + test.cpu_load.min
            sum_high = sum_high + test.cpu_load.max
            count = count + 1  
            
        mittel_high = sum_high / count;
        mittel_low = sum_low / count;
        mittel_average = sum_average / count;
        return round(mittel_average, 6), round(mittel_high, 6), round(mittel_low, 6)
          
    def getStandardabweichungCpuLoad(self):
        values_average = []
        values_high = []
        values_low = []
        for test in self.tests:
            values_average.append(test.cpu_load.average)
            values_high.append(test.cpu_load.max)
            values_low.append(test.cpu_load.min)
        return  round(statistics.pstdev(values_average), 4), round(statistics.pstdev(values_high), 4), round(statistics.pstdev(values_low), 4)

    def getArytmetischesMittelSystemCpuLoad(self):
        count = 0
        sum_high = 0
        sum_low = 0
        sum_average = 0
        for test in self.tests:
            sum_average = sum_average + test.system_cpu_load.average
            sum_low = sum_low + test.system_cpu_load.min
            sum_high = sum_high + test.system_cpu_load.max
            count = count + 1  
            
        mittel_high = sum_high / count;
        mittel_low = sum_low / count;
        mittel_average = sum_average / count;
        return round(mittel_average, 4), round(mittel_high, 4), round(mittel_low, 4)
          
    def getStandardabweichungSystemCpuLoad(self):
        values_average = []
        values_high = []
        values_low = []
        for test in self.tests:
            values_average.append(test.system_cpu_load.average)
            values_high.append(test.system_cpu_load.max)
            values_low.append(test.system_cpu_load.min)
        return  round(statistics.pstdev(values_average), 4), round(statistics.pstdev(values_high), 4), round(statistics.pstdev(values_low), 4)

    def getArytmetischesMittelRamLoad(self):
        count = 0
        sum_high = 0
        sum_low = 0
        sum_average = 0
        for test in self.tests:
            sum_average = sum_average + test.ram_load.average
            sum_low = sum_low + test.ram_load.min
            sum_high = sum_high + test.ram_load.max
            count = count + 1  
            
        mittel_high = sum_high / count;
        mittel_low = sum_low / count;
        mittel_average = sum_average / count;
        return round(mittel_average, 4),round( mittel_high, 4), round(mittel_low, 4)
          
    def getStandardabweichungRamLoad(self):
        values_average = []
        values_high = []
        values_low = []
        for test in self.tests:
            values_average.append(test.ram_load.average)
            values_high.append(test.ram_load.max)
            values_low.append(test.ram_load.min)
        return  round(statistics.pstdev(values_average), 4), round(statistics.pstdev(values_high), 4), round(statistics.pstdev(values_low), 4)


class DBTestSet:
    
    def __init__(self, name) :
        self.name = name
        self.testSets = []
    
    
    def addTest(self,testName, test):
        inserted = False
        for testSet in self.testSets:
            if(testSet.name == testName):
                testSet.addTest(test)
                inserted = True
        if inserted == False:
            set = TestSet(testName)
            set.addTest(test)
            self.testSets.append(set)

        


