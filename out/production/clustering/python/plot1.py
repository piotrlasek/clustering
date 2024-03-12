import pandas
import ggplot

with open('../../data/experiment/constraintsBrich1.txt', 'r') as f:
    content=f.readlines()
    
content = [x.strip() for x in content]


xlist = []
ylist = []
glist = []

group = 0;

for line in content:
    #group = group % 3
    cols = line.split('\t')
    xy = cols[0].split(',')
    x = xy[0]; xlist.append(int(x))
    y = xy[1]; ylist.append(int(y))
    glist.append(group)
    uv = cols[1].split(',')
    u = uv[0]; xlist.append(int(u))
    v = uv[1]; ylist.append(int(v))
    glist.append(group)
    group += 1

d = {'x': xlist, 'y': ylist, 'group': glist}

df = pandas.DataFrame(data=d)
