#!/bin/bash
# declare STRING variable
STRING="Hello World"
#print variable on a screen
echo "Start."

cp='-cp ".:../../../lib/*" Main'

wp="workspacepath=\eecs\home\plasek\clustering"
dt="data=data"
dtc="data=[CUSTOM]\data\checkins-pyramid"
al="algorithm"
kmeans="$al=kMeans"
pikmea="$al=pikMeans"
pr="parameters=dump"

f="Checkins"

echo "java $cp $wp $dtc $pikmea $pr;k:5;maxIterations:10;deepest:15;depth:15;starting:3;plot"
echo $cp
java $cp

java -cp ".:../../../lib/*" Main 


# $wp $dtc $pikmea $pr;k:5;maxIterations:10;deepest:15;depth:15;starting:3;plot


#java %cp% %wp% %dt%%f%.txt %kmeans% %pr%;k:5;maxIterations:10
#java %cp% %wp% %dtc%       %pikmea% %pr%;k:5;maxIterations:10;deepest:15;depth:15;starting:3;plot


