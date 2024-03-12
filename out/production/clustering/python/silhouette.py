# -*- coding: utf-8 -*-
"""
Created on Fri May 26 21:20:16 2017

@author: Piotr Lasek
"""

from sklearn import metrics
import numpy as np

# Testing

""" dimensions = 2
points_count = 10000
labels_count = 5;

labels = [random.sample(range(labels_count),1)[0] for x in range(points_count)]
X = [list(random.sample(range(100),dimensions)) for x in range(points_count)]
"""

workdir = r'C:\Users\Piotr\GitHubProjects\clustering\results'

files = open(workdir + r'\files-quality.txt')

lines = files.readlines()

for l in lines:
    file = l.strip()
    ss = '*'
    
    if file.find('(clusters=1)') == -1:
        print(file)
        a = np.genfromtxt(workdir + '\\' + file, delimiter=',')
        X = a[:,0:2]
        labels = a[:,2]
        ss = metrics.silhouette_score(X, labels, sample_size=30000, metric='euclidean')
        
    print(str(ss) + "\t" + file)