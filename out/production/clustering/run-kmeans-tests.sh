#!/cs/local/bin/bash

echo -----------------------------------------------------------------------
echo Start.
echo -----------------------------------------------------------------------

m=Main
cp='.:../../../lib/*'


wp="workspacepath=/local/data1/plasek/clustering"
dt="data=/data"
dtc="data=[CUSTOM]/data/checkins-pyramid/"
al=algorithm
kmeans="$al=kMeans"
pikmea="$al=pikMeans"
pr="parameters=\"dump"

f='/Checkins'

#r="java -Xss1024 -Xmx2048 -cp $cp $m $wp $dt$f.txt $kmeans $pr;k:7;maxIterations:10\""
#$r

r="java -Xms2048m -Xmx18192m -cp $cp $m $wp $dtc $pikmea $pr;k:5;maxIterations:40;deepest:15;depth:15;starting:3\""
echo $r
$r
