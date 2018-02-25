# -*- coding: utf-8 -*-
"""
Created on Sun Jan 21 18:23:07 2018

@author: Piotr
"""

import csv as csv
from sklearn import preprocessing
import pandas as pd
import numpy as np


df = pd.read_csv(
    r'C:\Users\piotr\Documents\projects\clustering\data\Checkins.csv',
    names=['x','y'])
mms = preprocessing.MinMaxScaler(copy=True, feature_range=(0,pow(2,32)))
mms.fit(df)

dfn = mms.transform(df)

#dfn = preprocessing.scale(df)
#np_scaled = min_max_scaler.fit_transform(df)
#dfn = pd.DataFrame(np_scaled)
#dfn = dfn - dfn.min()
#dfn

dfn = pd.DataFrame(dfn)

    

dfn.to_csv(r'C:\Users\piotr\Documents\projects\clustering\data\CheckinsN.csv',
    sep=',', index=False)
