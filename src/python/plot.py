# -*- coding: utf-8 -*-
"""
Created on Thu Apr 18 17:38:14 2019

@author: plasek
"""

import pandas as pd
import matplotlib.pyplot as plt
from matplotlib.patches import Rectangle
from matplotlib.patches import Circle

def get_spaced_colors(n):
    max_value = 16581375 #255**3
    interval = int(max_value / n)
    colors = [hex(I)[2:].zfill(6) for I in range(0, max_value, interval)]
    
    return [(int(i[:2], 16), int(i[2:4], 16), int(i[4:], 16)) for i in colors]

colors = get_spaced_colors(10)

# pts
cp = r'C:\Users\piotr\IdeaProjects\clustering\data\CheckinsN.csv'
dfp = pd.read_csv(cp, names=['x', 'y'])

#dfp = dfp.sample(900000)

# res
cr = r'C:\Users\piotr\IdeaProjects\clustering\results\CheckinsN_pikMeans_(k=8, mi=5)_iter_4.csv'

dfr = pd.read_csv(cr)

x, y = dfp['x'], dfp['y']

fig, ax = plt.subplots()

fig.set_figheight(12)
fig.set_figwidth(12)

ax.set_xlim(0, pow(2,16)-1)
ax.set_ylim(0, pow(2,16)-1)
#ax.scatter(figsize=(12,12))

ax.scatter(x, y, facecolor='black', s=1, alpha=0.2)

for i, r in dfr.iterrows():
    cid = int(r['id'])
    
    if cid != -2:
        c = 'C{}'.format(cid)
        #print(c)
        rect = Rectangle((r['x'], r['y']), r['s'], r['s'], edgecolor='gray',
                         facecolor=c, alpha = 0.25)
        ax.add_patch(rect)
    else:
        c = 'C{}'.format(cid)
        rect = Circle((r['x'], r['y']), 300, edgecolor='red', facecolor='None', alpha = 1)
        ax.add_patch(rect)

plt.show()