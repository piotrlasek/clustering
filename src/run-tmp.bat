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


echo QUALITY

echo -----------------------------------------------------------------------
set f=birch1

java %cp% %wp% %dt%\%f%.txt %_nbc% %pr%;k:50;
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:50;ic:%f%
java %cp% %wp% %dt%\%f%.txt %_nbc% %pr%;k:100;
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:100;ic:%f%
java %cp% %wp% %dt%\%f%.txt %_nbc% %pr%;k:150;
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:150;ic:%f

java %cp% %wp% %dt%\%f%.txt %_dbs% %pr%;Eps:4000;MinPts:15;
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:4000;MinPts:15;ic:%f%
java %cp% %wp% %dt%\%f%.txt %_dbs% %pr%;Eps:5000;MinPts:15;
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:5000;MinPts:15;ic:%f%
java %cp% %wp% %dt%\%f%.txt %_dbs% %pr%;Eps:6000;MinPts:15;
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:6000;MinPts:15;ic:%f%

echo -----------------------------------------------------------------------
set f=birch2

java %cp% %wp% %dt%\%f%.txt %_nbc% %pr%;k:30;
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:30;ic:%f%
java %cp% %wp% %dt%\%f%.txt %_nbc% %pr%;k:50;
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:50;ic:%f%
java %cp% %wp% %dt%\%f%.txt %_nbc% %pr%;k:70;
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:70;ic:%f%

java %cp% %wp% %dt%\%f%.txt %_dbs% %pr%;Eps:800;MinPts:15;
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:800;MinPts:15;ic:%f%
java %cp% %wp% %dt%\%f%.txt %_dbs% %pr%;Eps:1000;MinPts:15;
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:1000;MinPts:15;ic:%f%
java %cp% %wp% %dt%\%f%.txt %_dbs% %pr%;Eps:1200;MinPts:15;
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:1200;MinPts:15;ic:%f%

echo -----------------------------------------------------------------------
set f=birch3

java %cp% %wp% %dt%\%f%.txt %_nbc% %pr%;k:30;
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:30;ic:%f%
java %cp% %wp% %dt%\%f%.txt %_nbc% %pr%;k:50;
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:50;ic:%f%
java %cp% %wp% %dt%\%f%.txt %_nbc% %pr%;k:70;
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:70;ic:%f%

java %cp% %wp% %dt%\%f%.txt %_dbs% %pr%;Eps:5000;MinPts:15;
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:5000;MinPts:15;ic:%f%
java %cp% %wp% %dt%\%f%.txt %_dbs% %pr%;Eps:6000;MinPts:15;
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:6000;MinPts:15;ic:%f%
java %cp% %wp% %dt%\%f%.txt %_dbs% %pr%;Eps:7000;MinPts:15;
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:7000;MinPts:15;ic:%f%

echo EFFICIENCY

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
