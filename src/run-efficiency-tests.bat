echo off
echo -----------------------------------------------------------------------
echo Start.
echo -----------------------------------------------------------------------
set cp=-classpath ..\..\..\lib\*;. Main
set wp=workspacepath=C:\Users\Piotr\GitHubProjects\clustering
set dt=data=\data\experiment
set al=algorithm
set _nbc=%al%=NBC
set cnbc=%al%=C-NBC
set _dbs=%al%=DBSCAN
set cdbs=%al%=C-DBSCAN
set pr=parameters=dump;plot;close_plot


echo EFFICIENCY
echo -----------------------------------------------------------------------

set f=birch1
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:100;ic:random_10
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:100;ic:random_20
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:100;ic:random_40
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:100;ic:random_60
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:100;ic:random_80
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:100;ic:random_100
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:5000;MinPts:15;ic:random_10
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:5000;MinPts:15;ic:random_20
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:5000;MinPts:15;ic:random_40
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:5000;MinPts:15;ic:random_60
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:5000;MinPts:15;ic:random_80
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:5000;MinPts:15;ic:random_100

set f=birch2
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:50;ic:random_10
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:50;ic:random_20
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:50;ic:random_40
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:50;ic:random_60
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:50;ic:random_80
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:50;ic:random_100
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:1000;MinPts:15;ic:random_10
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:1000;MinPts:15;ic:random_20
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:1000;MinPts:15;ic:random_40
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:1000;MinPts:15;ic:random_60
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:1000;MinPts:15;ic:random_80
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:1000;MinPts:15;ic:random_100

set f=birch3
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:50;ic:random_10
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:50;ic:random_20
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:50;ic:random_40
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:50;ic:random_60
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:50;ic:random_80
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:50;ic:random_100
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:6000;MinPts:15;ic:random_10
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:6000;MinPts:15;ic:random_20
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:6000;MinPts:15;ic:random_40
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:6000;MinPts:15;ic:random_60
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:6000;MinPts:15;ic:random_80
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:6000;MinPts:15;ic:random_100

echo -----------------------------------------------------------------------
echo Done.
echo -----------------------------------------------------------------------
