import datetime

date_time_str = '2022-12-01T10:27:03.929149'
# strptime(input_string, input_format)
date_time_obj = datetime.datetime.strptime(date_time_str, '%Y-%m-%dT%H:%M:%S.%f')

print('Date:', date_time_obj.date())
print('Time:', date_time_obj.time())
print('Date-time:', date_time_obj)
