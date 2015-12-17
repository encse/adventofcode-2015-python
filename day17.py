import fileinput

def get_combinations(containers, target):
	if target == 0:
		yield []
	elif target < 0 or len(containers) == 0:
		return
	else:
		for res in get_combinations(containers[1:], target):
			yield res
		for res in get_combinations(containers[1:], target - containers[0]):
		 	yield [containers[0]] + res
	
containers = []
for line in fileinput.input():
	containers.append(int(line))

combinations = list(get_combinations(containers, 150))
min_length = min(map(len, combinations))
print len(combinations)
print len([combination for combination in combinations if len(combination) == min_length])