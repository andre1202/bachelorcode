from datetime import datetime 

datetime_str = '09/19/18 13:55:26'

datetime_object = datetime.strptime(datetime_str, '%m/%d/%y %H:%M:%S')

print(type(datetime_object))
print(datetime_object)  # printed in default format

date_str = '09-19-2018'

date_object = datetime.strptime(date_str, '%m-%d-%Y').date()
print(type(date_object))
print(date_object)  # printed in default formatting

date_str = '2022-05-12'

date_object = datetime.strptime(date_str, '%Y-%m-%d').date()
print(type(date_object))
print(date_object)  # printed in default formatting

def generateTimeStame(date, time):
    datetime_object = datetime.strptime(f'{date} {time}', '%Y-%m-%d %H:%M:%S')
    timeStamp = datetime.timestamp(datetime_object)
    return timeStamp
    

date = "2022-05-12"
time = "15:15:11"
print(generateTimeStame(date, time))