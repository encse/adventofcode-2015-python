import fileinput

T = 2503
def reindeer(v, t1, t2):
	state = {'d':0, 't':0}
	def step():
		state['t'] += 1
		if state['t'] <= t1:
			state['d']+= v
		if state['t'] == t1 + t2:
			state['t'] = 0
		return state['d']
	return step

reindeers = []
for line in fileinput.input():
	#Dancer can fly 27 km/s for 5 seconds, but then must rest for 132 seconds.
	m = line.split(' ')
	(v, t1, t2) = (int(m[3]), int(m[6]), int(m[13]))
	reindeers.append(reindeer(v,t1,t2))

distances = []
points = [0] * len(reindeers)
for t in xrange(0,T):
	distances = map(lambda r: r(), reindeers)	
	max_distance = max(distances)
	points_delta = map(lambda d: 1 if d == max_distance else 0, distances)
	points = map(sum, zip(points, points_delta))

print max(distances)
print max(points)
