# -*- coding: utf-8 -*-
"""
Created on Sun Jan 21 18:23:07 2018

@author: Piotr
"""

import csv as csv
from sklearn import preprocessing
import pandas as pd
import numpy as np


df = pd.read_csv(r'C:\Users\Piotr\GitHubProjects\tmp\clustering\data\Checkins.csv')

min_max_scaler = preprocessing.MinMaxScaler(feature_range=(0, 100))
np_scaled = min_max_scaler.fit_transform(df)
dfn = pd.DataFrame(np_scaled)
dfn = dfn - dfn.min()

dfn.to_csv(r'C:\Users\Piotr\GitHubProjects\tmp\clustering\data\CheckinsN.csv', sep=',', index=False)
