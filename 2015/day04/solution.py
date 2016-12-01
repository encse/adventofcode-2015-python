import hashlib

def find(input, prefix):
	res = 0
	while True:
		m = hashlib.md5()
		m.update(input + str(res))
		key =  m.hexdigest()
		if key.startswith(prefix):
			break
		res += 1
	return res

input = 'yzbqklnj' 
print find(input, '00000')
print find(input, '000000')