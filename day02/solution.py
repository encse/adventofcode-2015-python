import fileinput

paper = 0;
ribbon = 0
for line in fileinput.input():
    (w,h,l) = [int(x) for x in line.split('x')]
    sides = [w*h, h*l, w*l]
    min_side = min(sides) 
    paper +=  2* sum(sides) + min_side

    min_perimeter = 2 * sum(sorted([w,h,l])[:2])
    cube = w*h*l
    ribbon += min_perimeter + cube
print (paper, ribbon)