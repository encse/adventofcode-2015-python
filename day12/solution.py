import fileinput
import json

def process(obj, filter):
	sum = 0;
  	if isinstance(obj, dict):
  		if filter(obj):
  			for key in obj:
  				sum += process(obj[key], filter)
	
	elif isinstance(obj, list):
		for item in obj:
			sum += process(item, filter)
	elif isinstance(obj, int):
		sum += obj

	return sum

input = json.loads(fileinput.input()[0])
print process(input, lambda x: True)
print process(input, lambda x: 'red' not in x.values())
