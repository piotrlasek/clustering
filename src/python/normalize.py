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
    r'C:\Users\piotr\IdeaProjects\clustering\data\Checkins.csv',
    names=['x','y'])

df = df[df.columns.tolist()[::-1]]
# Range should be something like 2^16 due the Morton2D code
# limitations
mms = preprocessing.MinMaxScaler(copy=True, feature_range=(0,pow(2,16)-1))
mms.fit(df)

dfn = mms.transform(df)

#dfn = preprocessing.scale(df)
#np_scaled = min_max_scaler.fit_transform(df)
#dfn = pd.DataFrame(np_scaled)
#dfn = dfn - dfn.min()
#dfn

dfn = pd.DataFrame(dfn)

    

dfn.to_csv(r'C:\Users\piotr\IdeaProjects\clustering\data\CheckinsN.csv',
    sep=',', index=False)
