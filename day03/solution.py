import fileinput

def step((x,y), visited, dir):

    if dir == '>':
        x += 1
    if dir == '<':
        x -= 1
    if dir == '^':
        y -= 1
    if dir == 'v':
        y += 1

    visited.add((x,y))
    return (x,y)

def run(steps, visited):
    coord = (0,0)
    visited.add(coord)
    for dir in steps:
        coord = step(coord, visited, dir)

    return visited

steps = fileinput.input()[0].strip()

print len(run(steps, set())) 
print len(run(steps[::2], run(steps[1::2], set())))